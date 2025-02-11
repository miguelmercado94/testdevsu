package com.devsu.micropersona.dominio.servicio;

import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import com.devsu.micropersona.bd.entidad.Cliente;
import com.devsu.micropersona.bd.repositorio.ClienteRepositorio;
import com.devsu.micropersona.dominio.dto.ClienteDto;
import com.devsu.micropersona.dominio.dto.CuentaDto;
import com.devsu.micropersona.dominio.mapper.ClienteMapper;
import com.devsu.micropersona.dominio.util.UtilServicio;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;

@Service
public class ClienteServicio implements IClienteServicio {

    @Autowired
    private ClienteRepositorio repoCliente;

    @Autowired
    private ClienteMapper mapper;

    @Autowired
    private WebClient.Builder webClientBuilder;

    @Autowired
    private ObjectMapper objectMapper;

    private final String cuentaDevsuUrl = "http://cuentadevsu:8086/devsu/api/cuentas/v1/cliente";

    private final HttpClient httpClient;

    public ClienteServicio(WebClient.Builder webClientBuilder, ObjectMapper objectMapper) {
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
    public ClienteDto crearNuevoCliente(ClienteDto cliente) {
        Cliente auxCliente = repoCliente.save(mapper.toCliente(cliente));
        if (auxCliente.getClienteId() > 0L) {
            return mapper.toClienteDTORequest(auxCliente);
        }
        return null;
    }

    @Override
    public ClienteDto actualizarCliente(ClienteDto cliente) {
        Cliente oldCliente = repoCliente.findByIdentificacion(cliente.getIdentificacion()).orElse(null);

        if (oldCliente != null) {
            oldCliente.setNombre(cliente.getNombre());
            oldCliente.setEdad(cliente.getEdad());
            oldCliente.setGenero(cliente.getGenero());
            oldCliente.setDireccion(cliente.getDireccion());
            oldCliente.setTelefono(cliente.getTelefono());
            oldCliente.setContrasena(cliente.getContrasena());
            oldCliente.setEstado(cliente.getEstado());

            Cliente clienteActualizado = repoCliente.save(oldCliente);
            return mapper.toClienteDTORequest(clienteActualizado);
        }

        return null;
    }

    @Override
    public void eliminarCliente(String identificacion) throws Exception {
        Cliente auxCliente = repoCliente.findByIdentificacion(identificacion).orElse(null);
        
        if (auxCliente != null ) {
            repoCliente.delete(auxCliente);
            eliminarCuentasCliente(identificacion);
        }
    }

    @Override
    public ClienteDto obtenerCliente(String identificacion) {
        Optional<Cliente> cliente = repoCliente.findByIdentificacion(identificacion);

        if (cliente.isPresent()) {
            ClienteDto clienteAux = mapper.toClienteDTORequest(cliente.get());
            return clienteAux;
        }

        return null;
    }

    @Override
    public ClienteDto obtenerClienteFull(String identificacion) {
        Optional<Cliente> cliente = repoCliente.findByIdentificacion(identificacion);

        if (cliente.isPresent()) {
            ClienteDto clienteAux = mapper.toClienteDTORequest(cliente.get());
            clienteAux.setCuentas(getCuentaCliente(identificacion));
            return clienteAux;
        }

        return null;
    }

    @Override
    public List<ClienteDto> obtenerTodosClienteSinDatosCuenta() {
        return mapper.toDtoList(repoCliente.findAll());
    }

    @Override
    public List<ClienteDto> obtenerTodosClienteDatosCuenta() {
        return mapper.toDtoList(repoCliente.findAll()).stream().map(cliente -> {
            cliente.setCuentas(getCuentaCliente(cliente.getIdentificacion()));
            return cliente;
        }).collect(Collectors.toList());
    }

    @Override
    public ClienteDto obtenerClienteYReporteCuenta(String identificacion, String numeroCuenta,
            Date fechaInicial, Date fechaFinal) {
        Optional<Cliente> cliente = repoCliente.findByIdentificacion(identificacion);

        if (cliente.isPresent()) {
            ClienteDto clienteAux = mapper.toClienteDTORequest(cliente.get());

            CuentaDto cuentaAux = getCuentaReporteCliente(identificacion, numeroCuenta,
                    UtilServicio.DateToString(fechaInicial, null), UtilServicio.DateToString(fechaFinal, null));

            clienteAux.setCuentas(Arrays.asList(cuentaAux));

            return clienteAux;
        }

        return null;
    }

    private void eliminarCuentasCliente(String identificacion) throws Exception {
        String uri = String.format("/%s", identificacion);
   
        ResponseEntity<?> response = realizarLlamadaRest(HttpMethod.DELETE, uri, Void.class);
        if(response.getStatusCode().is5xxServerError())
        throw new Exception("No se pudo conectar al microservicio de cuentas"); // Re-lanzar otras excepciones
        
    }
    
    private List<CuentaDto> getCuentaCliente(String identificacion) {
        String uri = String.format("/%s", identificacion);
        return Arrays.asList(realizarLlamadaRest(HttpMethod.GET, uri, CuentaDto[].class).getBody());
    }

    private CuentaDto getCuentaReporteCliente(String identificacion, String numeroCuenta, String fechaInicial,
            String fechaFinal) {
        String uri = String.format("/%s?numeroCuenta=%s&fechaInicial=%s&fechaFinal=%s",
                identificacion, numeroCuenta, fechaInicial, fechaFinal);
        return realizarLlamadaRest(HttpMethod.GET, uri, CuentaDto.class).getBody();
    }

    private <T> ResponseEntity<T> realizarLlamadaRest(HttpMethod metodo, String uri, Class<T> responseType) {
        try {
            return webClientBuilder.clientConnector(new ReactorClientHttpConnector(httpClient))
                .baseUrl(cuentaDevsuUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .build()
                .method(metodo)
                .uri(uri)
                .retrieve()
                .toEntity(responseType)
                .block();
        } catch (WebClientResponseException e) {
            if (e.getStatusCode().is5xxServerError()) {
                // Log the error and return a fallback response
                System.err.println("Error connecting to cuenta microservice: " + e.getMessage());
                return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
            }
            throw e; // Re-throw other exceptions
        }
    }

}
