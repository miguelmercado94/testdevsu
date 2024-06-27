package com.devsu.microcuenta.servicio;

import com.devsu.microcuenta.bd.entidad.Cuenta;
import com.devsu.microcuenta.bd.entidad.Movimiento;
import com.devsu.microcuenta.bd.repositorio.CuentaRepositorio;
import com.devsu.microcuenta.bd.repositorio.MovimientoRepositorio;
import com.devsu.microcuenta.dominio.dto.*;
import com.devsu.microcuenta.dominio.mapper.*;
import com.devsu.microcuenta.dominio.servicio.*;
import com.devsu.microcuenta.dominio.util.TipoCuenta;
import com.devsu.microcuenta.dominio.util.TipoMovimiento;
import com.github.javafaker.Faker;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.reactive.function.client.WebClient;

import java.math.BigDecimal;
import java.util.Date;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
class MovimientoServicioTests {

    @Mock
    private MovimientoRepositorio movimientoRepositorio;

    @Mock
    private CuentaRepositorio cuentaRepositorio;

    @Mock
    private MapperMovimiento mapperMovimiento;
    
    @Mock
    private Cuenta cuentaMock;
    
    @Mock
    private MapperCuenta mapperCuenta;
    
    @Mock
    private WebClient.Builder webClientBuilder;

    @InjectMocks
    private MovimientoServicio movimientoServicio;
    
    @Mock
    private CuentaServicio cuentaServicio;

    private final Faker faker = new Faker();
    
    @BeforeEach
    public void setUp() {
        WebClient webClient = WebClient.builder().build();
        when(webClientBuilder.build()).thenReturn(webClient);
    }

    
    @Test
    public void obtenerMovimientoTest() {
        // Arrange
        Date fechaInicial = new Date(); // Obtiene la fecha y hora actuales
      
        Long idMovimiento = 1L;
        Movimiento movimiento = new Movimiento();
        movimiento.setId(idMovimiento);
        movimiento.setFecha(fechaInicial);
        movimiento.setSaldo(BigDecimal.TEN);
        movimiento.setValor(BigDecimal.TEN);
        movimiento.setTipoMovimiento(TipoMovimiento.CREDITO);
        
        MovimientoDTO movimientoDTO = new MovimientoDTO();
        movimientoDTO.setId(idMovimiento);
        movimientoDTO.setFecha(fechaInicial);
        movimientoDTO.setSaldo(BigDecimal.TEN);
        movimientoDTO.setValor(BigDecimal.TEN);
        movimientoDTO.setTipoMovimiento("CREDITO");

        when(movimientoRepositorio.findById(idMovimiento)).thenReturn(Optional.of(movimiento));
        when(mapperMovimiento.convertToDto(movimiento)).thenReturn(movimientoDTO);

        // Act
        MovimientoDTO resultado = movimientoServicio.obtenerMovimiento(idMovimiento);

        // Assert
        assertNotNull(resultado);
        assertEquals(idMovimiento, resultado.getId());
    }
    
    @Test
    public void agregarNuevoMovimientoTest() {
        // Arrange
     
        Cuenta cuenta = new Cuenta();
        cuenta.setId(1L);
        cuenta.setNumeroCuenta("1256");
        cuenta.setTipoCuenta(TipoCuenta.AHORRO);
        cuenta.setSaldoInicial(BigDecimal.ZERO);
        cuenta.setEstado("ACTIVO");
        cuenta.setCliente("123456");
        cuenta.setMovimientos(null);

      


        MovimientoDTO movimientoDTO = new MovimientoDTO();
        movimientoDTO.setCuentaId(1L);
        movimientoDTO.setTipoMovimiento("DEBITO");
        movimientoDTO.setSaldo(BigDecimal.TEN);
        movimientoDTO.setValor(BigDecimal.TEN);
        movimientoDTO.setId(1L);

        Movimiento movimiento = new Movimiento();
        movimiento.setId(1L);
        movimiento.setFecha(new Date());
        movimiento.setSaldo(BigDecimal.TEN);
        movimiento.setValor(BigDecimal.TEN);
        movimiento.setTipoMovimiento(TipoMovimiento.DEBITO);

        cuenta.setMovimientos(Arrays.asList(movimiento));
        
        when(cuentaRepositorio.findById(1L)).thenReturn(Optional.of(cuenta));

        when(movimientoRepositorio.save(any(Movimiento.class))).thenReturn(movimiento);
        when(mapperMovimiento.convertToEntity(any(MovimientoDTO.class))).thenReturn(movimiento);
        when(mapperMovimiento.convertToDto(any(Movimiento.class))).thenReturn(movimientoDTO);

        // Act
        MovimientoDTO resultado = movimientoServicio.agregarNuevoMovimiento(movimientoDTO);

        // Assert
        assertNotNull(resultado);
        assertNotNull(resultado.getFecha());
        assertNotNull(resultado.getId());
    }


    @Test
    public void obtenerMovimientosPorNumeroCuentaTest() {
        // Arrange
        String numeroCuenta = "123456";
        Cuenta cuenta = new Cuenta();
        cuenta.setNumeroCuenta(numeroCuenta);
        List<Movimiento> movimientos = Arrays.asList(new Movimiento(), new Movimiento());
        cuenta.setMovimientos(movimientos);
        when(cuentaRepositorio.findByNumeroCuenta(numeroCuenta)).thenReturn(cuenta);

        // Act
        List<MovimientoDTO> resultado = movimientoServicio.obtenerMovimientosPorNumeroCuenta(numeroCuenta);

        // Assert
        assertNotNull(resultado);
        assertEquals(movimientos.size(), resultado.size());
    }

    
    @Test
    void obtenerMovimientosPorCuentaYFecha() {
        Long idCuenta = faker.number().randomNumber();
        Date fechaInicial = faker.date().past(10, TimeUnit.DAYS);
        Date fechaFinal = faker.date().future(10, TimeUnit.DAYS);

        Cuenta cuentaMock = new Cuenta();
        cuentaMock.setId(idCuenta);
        cuentaMock.setNumeroCuenta(faker.finance().creditCard());
        cuentaMock.setTipoCuenta(TipoCuenta.AHORRO);
        cuentaMock.setSaldoInicial(BigDecimal.valueOf(faker.number().randomDouble(2, 100, 10000)));
        cuentaMock.setEstado("ACTIVO");
        cuentaMock.setCliente(faker.name().fullName());
        cuentaMock.setMovimientos(new ArrayList<>());

        when(cuentaRepositorio.findById(idCuenta)).thenReturn(Optional.of(cuentaMock));

        List<Movimiento> movimientosMock = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            Movimiento movimiento = new Movimiento();
            movimiento.setId(faker.number().randomNumber());
            movimiento.setFecha(faker.date().between(fechaInicial, fechaFinal));
            movimiento.setTipoMovimiento(faker.options().option(TipoMovimiento.class));
            movimiento.setValor(BigDecimal.valueOf(faker.number().randomDouble(2, 10, 1000)));
            movimiento.setSaldo(BigDecimal.valueOf(faker.number().randomDouble(2, 100, 10000)));
            movimiento.setCuenta(cuentaMock);
            movimientosMock.add(movimiento);
        }

        when(movimientoRepositorio.findByCuentaNumeroCuentaAndFechaBetween(cuentaMock.getNumeroCuenta(), fechaInicial, fechaFinal)).thenReturn(movimientosMock);

        List<MovimientoDTO> movimientosDTO = movimientoServicio.obtenerMovimientosPorCuentaYFecha(cuentaMock.getNumeroCuenta(), fechaInicial, fechaFinal);

        assertEquals(movimientosMock.size(), movimientosDTO.size());
    }


}
