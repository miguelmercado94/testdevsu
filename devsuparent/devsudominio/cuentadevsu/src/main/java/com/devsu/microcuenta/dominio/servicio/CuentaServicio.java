package com.devsu.microcuenta.dominio.servicio;

import java.time.Duration;
import java.util.Date;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.devsu.microcuenta.bd.entidad.Cuenta;
import com.devsu.microcuenta.bd.repositorio.CuentaRepositorio;
import com.devsu.microcuenta.dominio.dto.CuentaDto;
import com.devsu.microcuenta.dominio.dto.MovimientoDTO;
import com.devsu.microcuenta.dominio.exception.FechaFueraDeRangoException;
import com.devsu.microcuenta.dominio.mapper.MapperCuenta;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import reactor.netty.http.client.HttpClient;

@Service
public class CuentaServicio implements ICuentaServicio {

    @Autowired
    private CuentaRepositorio repoCuenta;

    @Autowired
    private MapperCuenta mapperCuenta;

    @Autowired
    private IMovimientoServicio movimientoServicio;

    private final String personaUrl = "http://personadevsu:8087/devsu/api/clientes/v1/";

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private WebClient.Builder webClientBuilder;

    private final HttpClient httpClient;

    public CuentaServicio(WebClient.Builder webClientBuilder, ObjectMapper objectMapper) {
        this.webClientBuilder = webClientBuilder;
        this.objectMapper = objectMapper;

        this.httpClient = HttpClient.create()
            .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 5000)
            .option(ChannelOption.SO_KEEPALIVE, true)
            .responseTimeout(Duration.ofSeconds(1))
            .doOnConnected(connection -> {
                connection.addHandlerLast(new ReadTimeoutHandler(5000, TimeUnit.MILLISECONDS));
                connection.addHandlerLast(new WriteTimeoutHandler(5000, TimeUnit.MILLISECONDS));
            });
    }

    @Override
    public CuentaDto agregarNuevaCuenta(CuentaDto cuentaDto) {
        List<MovimientoDTO> movimientos = cuentaDto.getMovimientos();
        cuentaDto.setMovimientos(null);

        if (verificarCliente(cuentaDto.getCliente())) {
            Cuenta cuentaAux = repoCuenta.save(mapperCuenta.convertToCuentaEntity(cuentaDto));

            if (cuentaAux != null && cuentaAux.getId() > 0l) {
                CuentaDto dto = mapperCuenta.convertToCuentaDto(cuentaAux);

                if (movimientos != null && !movimientos.isEmpty()) {
                    List<MovimientoDTO> movimientosAux = new ArrayList<MovimientoDTO>();
                    for (MovimientoDTO movimientoDTO : movimientos) {
                        movimientoDTO.setCuentaId(cuentaAux.getId());
                        movimientosAux.add(movimientoServicio.agregarNuevoMovimiento(movimientoDTO));
                    }
                    dto.setMovimientos(movimientosAux);
                }

                return dto;
            }
        }

        return null;
    }

    @Override
    public CuentaDto actualizarCuenta(CuentaDto cuentaDto) {
        Cuenta cuentaOld = repoCuenta.findById(cuentaDto.getId()).orElse(null);

        if (cuentaOld != null) {
            cuentaOld.setCliente(cuentaDto.getCliente());
            cuentaOld.setEstado(cuentaDto.getEstado());
            cuentaOld.setNumeroCuenta(cuentaDto.getNumeroCuenta());
            cuentaOld.setTipoCuenta(cuentaDto.getTipoCuenta());

            return mapperCuenta.convertToCuentaDto(repoCuenta.save(cuentaOld));
        }

        return null;
    }

    @Override
    public CuentaDto obtenerCuentaPorNumero(String numeroCuenta) {
        return mapperCuenta.convertToCuentaDto(repoCuenta.findByNumeroCuenta(numeroCuenta));
    }

    @Override
    public List<CuentaDto> obtenerCuentasPorCliente(String identificacion) {
        return repoCuenta.findByCliente(identificacion).stream()
                .map(mapperCuenta::convertToCuentaDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<CuentaDto> obtenerTodaslasCuentas() {
        Iterable<Cuenta> cuentasIterable = repoCuenta.findAll();
        return StreamSupport.stream(cuentasIterable.spliterator(), false)
                .map(mapperCuenta::convertToCuentaDto)
                .collect(Collectors.toList());
    }

    @Override
    public void eliminarCuenta(String numeroCuenta) {
        Cuenta cuentaAux = repoCuenta.findByNumeroCuenta(numeroCuenta);
        if (cuentaAux != null) repoCuenta.delete(cuentaAux);
    }

    @Override
    public CuentaDto reporteMovimientosCuentaPorRangoFecha(String numeroCuenta, Date fechaInicial, Date fechaFinal) {
        if (fechaInicial.compareTo(fechaFinal) > 0) {
            throw new FechaFueraDeRangoException("La fecha inicial no puede ser posterior a la fecha final.");
        }

        CuentaDto cuentaAux = obtenerCuentaPorNumero(numeroCuenta);
        List<MovimientoDTO> movimientos = movimientoServicio.obtenerMovimientosPorCuentaYFecha(numeroCuenta, fechaInicial, fechaFinal);
        cuentaAux.setMovimientos(movimientos);

        return cuentaAux;
    }

    private boolean verificarCliente(String identificacion) {
        WebClient build = webClientBuilder.clientConnector(new ReactorClientHttpConnector(httpClient))
                .baseUrl(personaUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultUriVariables(Collections.singletonMap("url", personaUrl))
                .build();

        String uri = String.format("/%s", identificacion);

        try {
            JsonNode block = build.method(HttpMethod.GET)
                    .uri(uri)
                    .retrieve()
                    .bodyToMono(JsonNode.class)
                    .block();
            return block != null && !block.isEmpty();
        } catch (WebClientResponseException e) {
            if (e.getStatusCode().is5xxServerError()) {
                // Log the error and return a fallback response
                System.err.println("Error connecting to persona microservice: " + e.getMessage());
                return false;
            }
            throw e; // Re-throw other exceptions
        }
    }
}
