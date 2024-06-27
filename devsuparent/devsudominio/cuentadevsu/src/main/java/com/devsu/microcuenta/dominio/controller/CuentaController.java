package com.devsu.microcuenta.dominio.controller;

import com.devsu.microcuenta.dominio.dto.CuentaDto;
import com.devsu.microcuenta.dominio.servicio.CuentaServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/cuentas/v1")
@Api(value = "Sistema de Gestión de Cuentas", description = "Operaciones relacionadas con la gestión de cuentas")
public class CuentaController {

    @Autowired
    private CuentaServicio cuentaServicio;

    @ApiOperation(value = "Agregar una nueva cuenta")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Cuenta creada con éxito"),
            @ApiResponse(code = 400, message = "Solicitud incorrecta")
    })
    @PostMapping
    public ResponseEntity<CuentaDto> agregarNuevaCuenta(
            @ApiParam(value = "Detalles de la cuenta a crear", required = true)
            @RequestBody CuentaDto cuentaDto) {
        CuentaDto nuevaCuenta = cuentaServicio.agregarNuevaCuenta(cuentaDto);
        if (nuevaCuenta != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevaCuenta);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @ApiOperation(value = "Actualizar una cuenta existente")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Cuenta actualizada con éxito"),
            @ApiResponse(code = 404, message = "Cuenta no encontrada")
    })
    @PutMapping
    public ResponseEntity<CuentaDto> actualizarCuenta(
            @ApiParam(value = "Detalles de la cuenta a actualizar", required = true)
            @RequestBody CuentaDto cuentaDto) {
        CuentaDto cuentaActualizada = cuentaServicio.actualizarCuenta(cuentaDto);
        if (cuentaActualizada != null) {
            return ResponseEntity.ok(cuentaActualizada);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @ApiOperation(value = "Obtener una cuenta por número de cuenta")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Cuenta encontrada"),
            @ApiResponse(code = 404, message = "Cuenta no encontrada")
    })
    @GetMapping("/{numeroCuenta}")
    public ResponseEntity<CuentaDto> obtenerCuentaPorNumero(
            @ApiParam(value = "Número de cuenta a obtener", required = true)
            @PathVariable(name = "numeroCuenta") String numeroCuenta) {
        CuentaDto cuenta = cuentaServicio.obtenerCuentaPorNumero(numeroCuenta);
        if (cuenta != null) {
            return ResponseEntity.ok(cuenta);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @ApiOperation(value = "Obtener el reporte de movimientos de una cuenta por rango de fechas")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Reporte generado con éxito"),
            @ApiResponse(code = 409, message = "Conflicto al generar el reporte")
    })
    @GetMapping("/reporte")
    public ResponseEntity<?> obtenerCuentaPorRangoFecha(
            @ApiParam(value = "Número de cuenta a obtener el reporte", required = true)
            @RequestParam(name = "numeroCuenta") String numeroCuenta,
            @ApiParam(value = "Fecha inicial del reporte", required = true)
            @RequestParam(name = "fechaInicial") String fechaInicial,
            @ApiParam(value = "Fecha final del reporte", required = true)
            @RequestParam(name = "fechaFinal") String fechaFinal) {

        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            Date fechaIn = sdf.parse(fechaInicial);
            fechaIn.setHours(0);
            fechaIn.setMinutes(0);
            fechaIn.setSeconds(0);

            Date fechaFin = sdf.parse(fechaFinal);
            fechaFin.setHours(23);
            fechaFin.setMinutes(59);
            fechaFin.setSeconds(59);

            CuentaDto cuenta = cuentaServicio.reporteMovimientosCuentaPorRangoFecha(numeroCuenta, fechaIn, fechaFin);
            return ResponseEntity.ok(cuenta);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }

    @ApiOperation(value = "Obtener todas las cuentas de un cliente por identificación")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Cuentas encontradas"),
            @ApiResponse(code = 404, message = "Cliente no encontrado")
    })
    @GetMapping("/cliente/{identificacion}")
    public ResponseEntity<List<CuentaDto>> obtenerCuentasPorCliente(
            @ApiParam(value = "Identificación del cliente para obtener sus cuentas", required = true)
            @PathVariable(name = "identificacion") String identificacion) {
        List<CuentaDto> cuentas = cuentaServicio.obtenerCuentasPorCliente(identificacion);
        if (cuentas != null && !cuentas.isEmpty()) {
            return ResponseEntity.ok(cuentas);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @ApiOperation(value = "Elimina todas las cuentas de un cliente por identificación")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Cuentas eliminadas con éxito"),
            @ApiResponse(code = 404, message = "Cliente no encontrado"),
            @ApiResponse(code = 500, message = "Error interno del servidor")
    })
    @DeleteMapping("/cliente/{identificacion}")
    public ResponseEntity<?> eliminarCuentasPorCliente(
            @ApiParam(value = "Identificación del cliente para obtener sus cuentas", required = true)
            @PathVariable(name = "identificacion") String identificacion) {
        
        List<CuentaDto> cuentas = cuentaServicio.obtenerCuentasPorCliente(identificacion);
        
        if (cuentas == null || cuentas.isEmpty()) {
            return ResponseEntity.status(404).body("Cliente no encontrado o no tiene cuentas");
        }

        for (CuentaDto cuentaAux : cuentas) {
            try {
                cuentaServicio.eliminarCuenta(cuentaAux.getNumeroCuenta());
            } catch (Exception e) {
                return ResponseEntity.status(500).body("Error al eliminar la cuenta: " + cuentaAux.getNumeroCuenta());
            }
        }

        return ResponseEntity.ok("Cuentas eliminadas con éxito");
    }
    
    @ApiOperation(value = "Obtener todas las cuentas")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Cuentas encontradas"),
            @ApiResponse(code = 204, message = "No hay cuentas disponibles")
    })
    @GetMapping
    public ResponseEntity<List<CuentaDto>> obtenerTodasLasCuentas() {
        List<CuentaDto> cuentas = cuentaServicio.obtenerTodaslasCuentas();
        if (cuentas != null && !cuentas.isEmpty()) {
            return ResponseEntity.ok(cuentas);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @ApiOperation(value = "Eliminar una cuenta por número de cuenta")
    @ApiResponses(value = {
            @ApiResponse(code = 204, message = "Cuenta eliminada con éxito"),
            @ApiResponse(code = 404, message = "Cuenta no encontrada")
    })
    @DeleteMapping("/{numeroCuenta}")
    public ResponseEntity<Void> eliminarCuenta(
            @ApiParam(value = "Número de cuenta a eliminar", required = true)
            @PathVariable(name = "numeroCuenta") String numeroCuenta) {
        cuentaServicio.eliminarCuenta(numeroCuenta);
        return ResponseEntity.noContent().build();
    }
}
