package com.devsu.microcuenta.dominio.controller;

import com.devsu.microcuenta.dominio.dto.MovimientoDTO;
import com.devsu.microcuenta.dominio.servicio.MovimientoServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.*;

import java.util.List;

@RestController
@RequestMapping("/api/movimientos/v1")
@Api(value = "Sistema de Gestión de Movimientos", description = "Operaciones relacionadas con la gestión de movimientos")
public class MovimientoController {

    @Autowired
    private MovimientoServicio movimientoServicio;

    @ApiOperation(value = "Agregar un nuevo movimiento")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Movimiento creado con éxito"),
            @ApiResponse(code = 400, message = "Solicitud incorrecta")
    })
    @PostMapping
    public ResponseEntity<MovimientoDTO> agregarNuevoMovimiento(
            @ApiParam(value = "Detalles del movimiento a crear", required = true)
            @RequestBody MovimientoDTO dto) {
        MovimientoDTO nuevoMovimiento = movimientoServicio.agregarNuevoMovimiento(dto);
        if (nuevoMovimiento != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoMovimiento);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @ApiOperation(value = "Obtener un movimiento por ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Movimiento encontrado"),
            @ApiResponse(code = 404, message = "Movimiento no encontrado")
    })
    @GetMapping("/{id}")
    public ResponseEntity<MovimientoDTO> obtenerMovimiento(
            @ApiParam(value = "ID del movimiento a obtener", required = true)
            @PathVariable(name = "id") Long id) {
        MovimientoDTO movimiento = movimientoServicio.obtenerMovimiento(id);
        if (movimiento != null) {
            return ResponseEntity.ok(movimiento);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @ApiOperation(value = "Obtener todos los movimientos de una cuenta por ID de cuenta")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Movimientos encontrados"),
            @ApiResponse(code = 204, message = "No hay movimientos disponibles para la cuenta")
    })
    @GetMapping("/cuenta/{idCuenta}")
    public ResponseEntity<List<MovimientoDTO>> obtenerMovimientosPorCuenta(
            @ApiParam(value = "ID de la cuenta para obtener sus movimientos", required = true)
            @PathVariable(name = "idCuenta") Long idCuenta) {
        List<MovimientoDTO> movimientos = movimientoServicio.obtenerMovimientosPorCuenta(idCuenta);
        if (movimientos != null && !movimientos.isEmpty()) {
            return ResponseEntity.ok(movimientos);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @ApiOperation(value = "Obtener todos los movimientos de una cuenta por número de cuenta")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Movimientos encontrados"),
            @ApiResponse(code = 204, message = "No hay movimientos disponibles para la cuenta")
    })
    @GetMapping("/cuenta/numero/{numeroCuenta}")
    public ResponseEntity<List<MovimientoDTO>> obtenerMovimientosPorNumeroCuenta(
            @ApiParam(value = "Número de cuenta para obtener sus movimientos", required = true)
            @PathVariable(name = "numeroCuenta") String numeroCuenta) {
        List<MovimientoDTO> movimientos = movimientoServicio.obtenerMovimientosPorNumeroCuenta(numeroCuenta);
        if (movimientos != null && !movimientos.isEmpty()) {
            return ResponseEntity.ok(movimientos);
        } else {
            return ResponseEntity.noContent().build();
        }
    }
}
