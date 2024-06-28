package com.devsu.microcuenta.dominio.controller;

import com.devsu.microcuenta.dominio.dto.MovimientoDTO;
import com.devsu.microcuenta.dominio.servicio.MovimientoServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.util.List;

@RestController
@RequestMapping("/api/movimientos/v1")
@Tag(name = "Sistema de Gestión de Movimientos", description = "Operaciones relacionadas con la gestión de movimientos")
public class MovimientoController {

    @Autowired
    private MovimientoServicio movimientoServicio;

    @Operation(summary = "Agregar un nuevo movimiento")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Movimiento creado con éxito", 
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = MovimientoDTO.class))),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta", content = @Content)
    })
    @PostMapping
    public ResponseEntity<MovimientoDTO> agregarNuevoMovimiento(
            @RequestBody(description = "Detalles del movimiento a crear", required = true) 
             MovimientoDTO dto) {
        MovimientoDTO nuevoMovimiento = movimientoServicio.agregarNuevoMovimiento(dto);
        if (nuevoMovimiento != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(nuevoMovimiento);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Obtener un movimiento por ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Movimiento encontrado", 
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = MovimientoDTO.class))),
            @ApiResponse(responseCode = "404", description = "Movimiento no encontrado", content = @Content)
    })
    @GetMapping("/{id}")
    public ResponseEntity<MovimientoDTO> obtenerMovimiento(
            @Parameter(description = "ID del movimiento a obtener", required = true)
            @PathVariable(name = "id") Long id) {
        MovimientoDTO movimiento = movimientoServicio.obtenerMovimiento(id);
        if (movimiento != null) {
            return ResponseEntity.ok(movimiento);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Obtener todos los movimientos de una cuenta por ID de cuenta")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Movimientos encontrados", 
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = MovimientoDTO.class))),
            @ApiResponse(responseCode = "204", description = "No hay movimientos disponibles para la cuenta", content = @Content)
    })
    @GetMapping("/cuenta/{idCuenta}")
    public ResponseEntity<List<MovimientoDTO>> obtenerMovimientosPorCuenta(
            @Parameter(description = "ID de la cuenta para obtener sus movimientos", required = true)
            @PathVariable(name = "idCuenta") Long idCuenta) {
        List<MovimientoDTO> movimientos = movimientoServicio.obtenerMovimientosPorCuenta(idCuenta);
        if (movimientos != null && !movimientos.isEmpty()) {
            return ResponseEntity.ok(movimientos);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @Operation(summary = "Obtener todos los movimientos de una cuenta por número de cuenta")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Movimientos encontrados", 
                         content = @Content(mediaType = "application/json", schema = @Schema(implementation = MovimientoDTO.class))),
            @ApiResponse(responseCode = "204", description = "No hay movimientos disponibles para la cuenta", content = @Content)
    })
    @GetMapping("/cuenta/numero/{numeroCuenta}")
    public ResponseEntity<List<MovimientoDTO>> obtenerMovimientosPorNumeroCuenta(
            @Parameter(description = "Número de cuenta para obtener sus movimientos", required = true)
            @PathVariable(name = "numeroCuenta") String numeroCuenta) {
        List<MovimientoDTO> movimientos = movimientoServicio.obtenerMovimientosPorNumeroCuenta(numeroCuenta);
        if (movimientos != null && !movimientos.isEmpty()) {
            return ResponseEntity.ok(movimientos);
        } else {
            return ResponseEntity.noContent().build();
        }
    }
}
