package com.devsu.microcuenta.dominio.controller;

import com.devsu.microcuenta.dominio.dto.CuentaDto;
import com.devsu.microcuenta.dominio.servicio.CuentaServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/cuentas/v1")
@Tag(name = "Sistema de Gestión de Cuentas", description = "Operaciones relacionadas con la gestión de cuentas")
public class CuentaController {

	@Autowired
	private CuentaServicio cuentaServicio;

	@Operation(summary = "Agregar una nueva cuenta")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Cuenta creada con éxito", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CuentaDto.class))),
			@ApiResponse(responseCode = "400", description = "Solicitud incorrecta", content = @Content) })
	@PostMapping
	public ResponseEntity<CuentaDto> agregarNuevaCuenta(
			@RequestBody(description = "Detalles de la cuenta a crear", required = true) CuentaDto cuentaDto) {
		CuentaDto nuevaCuenta = cuentaServicio.agregarNuevaCuenta(cuentaDto);
		if (nuevaCuenta != null) {
			return ResponseEntity.status(HttpStatus.CREATED).body(nuevaCuenta);
		} else {
			return ResponseEntity.badRequest().build();
		}
	}

	@Operation(summary = "Actualizar una cuenta existente")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Cuenta actualizada con éxito", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CuentaDto.class))),
			@ApiResponse(responseCode = "404", description = "Cuenta no encontrada", content = @Content) })
	@PutMapping
	public ResponseEntity<CuentaDto> actualizarCuenta(
			@RequestBody(description = "Detalles de la cuenta a actualizar", required = true) CuentaDto cuentaDto) {
		CuentaDto cuentaActualizada = cuentaServicio.actualizarCuenta(cuentaDto);
		if (cuentaActualizada != null) {
			return ResponseEntity.ok(cuentaActualizada);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@Operation(summary = "Obtener una cuenta por número de cuenta")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Cuenta encontrada", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CuentaDto.class))),
			@ApiResponse(responseCode = "404", description = "Cuenta no encontrada", content = @Content) })
	@GetMapping("/{numeroCuenta}")
	public ResponseEntity<CuentaDto> obtenerCuentaPorNumero(@PathVariable(name = "numeroCuenta") String numeroCuenta) {
		CuentaDto cuenta = cuentaServicio.obtenerCuentaPorNumero(numeroCuenta);
		if (cuenta != null) {
			return ResponseEntity.ok(cuenta);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@Operation(summary = "Obtener el reporte de movimientos de una cuenta por rango de fechas")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Reporte generado con éxito", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CuentaDto.class))),
			@ApiResponse(responseCode = "409", description = "Conflicto al generar el reporte", content = @Content) })
	@GetMapping("/reporte")
	public ResponseEntity<?> obtenerCuentaPorRangoFecha(@RequestParam(name = "numeroCuenta") String numeroCuenta,
			@RequestParam(name = "fechaInicial") String fechaInicial,
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

	@Operation(summary = "Obtener todas las cuentas de un cliente por identificación")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Cuentas encontradas", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CuentaDto.class))),
			@ApiResponse(responseCode = "404", description = "Cliente no encontrado", content = @Content) })
	@GetMapping("/cliente/{identificacion}")
	public ResponseEntity<List<CuentaDto>> obtenerCuentasPorCliente(
			@PathVariable(name = "identificacion") String identificacion) {
		List<CuentaDto> cuentas = cuentaServicio.obtenerCuentasPorCliente(identificacion);
		if (cuentas != null && !cuentas.isEmpty()) {
			return ResponseEntity.ok(cuentas);
		} else {
			return ResponseEntity.notFound().build();
		}
	}

	@Operation(summary = "Eliminar todas las cuentas de un cliente por identificación")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Cuentas eliminadas con éxito", content = @Content),
			@ApiResponse(responseCode = "404", description = "Cliente no encontrado o no tiene cuentas", content = @Content),
			@ApiResponse(responseCode = "500", description = "Error interno del servidor", content = @Content) })
	@DeleteMapping("/cliente/{identificacion}")
	public ResponseEntity<?> eliminarCuentasPorCliente(@PathVariable(name = "identificacion") String identificacion) {

		List<CuentaDto> cuentas = cuentaServicio.obtenerCuentasPorCliente(identificacion);

		if (cuentas == null || cuentas.isEmpty()) {
			return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente no encontrado o no tiene cuentas");
		}

		for (CuentaDto cuentaAux : cuentas) {
			try {
				cuentaServicio.eliminarCuenta(cuentaAux.getNumeroCuenta());
			} catch (Exception e) {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
						.body("Error al eliminar la cuenta: " + cuentaAux.getNumeroCuenta());
			}
		}

		return ResponseEntity.ok("Cuentas eliminadas con éxito");
	}

	@Operation(summary = "Obtener todas las cuentas")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Cuentas encontradas", content = @Content(mediaType = "application/json", schema = @Schema(implementation = CuentaDto.class))),
			@ApiResponse(responseCode = "204", description = "No hay cuentas disponibles", content = @Content) })
	@GetMapping
	public ResponseEntity<List<CuentaDto>> obtenerTodasLasCuentas() {
		List<CuentaDto> cuentas = cuentaServicio.obtenerTodaslasCuentas();
		if (cuentas != null && !cuentas.isEmpty()) {
			return ResponseEntity.ok(cuentas);
		} else {
			return ResponseEntity.noContent().build();
		}
	}

	@Operation(summary = "Eliminar una cuenta por número de cuenta")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "204", description = "Cuenta eliminada con éxito", content = @Content),
			@ApiResponse(responseCode = "404", description = "Cuenta no encontrada", content = @Content) })
	@DeleteMapping("/{numeroCuenta}")
	public ResponseEntity<Void> eliminarCuenta(@PathVariable(name = "numeroCuenta") String numeroCuenta) {
		cuentaServicio.eliminarCuenta(numeroCuenta);
		return ResponseEntity.noContent().build();
	}
}
