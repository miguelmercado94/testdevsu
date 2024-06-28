package com.devsu.micropersona.dominio.controller;

import com.devsu.micropersona.dominio.dto.ClienteDto;
import com.devsu.micropersona.dominio.servicio.IClienteServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.Parameter;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/api/clientes/v1")
public class ClienteController {

    @Autowired
    private IClienteServicio clienteServicio;

    @Operation(summary = "Crear un nuevo cliente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Cliente creado con éxito"),
            @ApiResponse(responseCode = "400", description = "Solicitud incorrecta")
    })
    @PostMapping
    public ResponseEntity<ClienteDto> crearNuevoCliente(
            @RequestBody(description = "Detalles del cliente a crear", required = true, content = @Content(schema = @Schema(implementation = ClienteDto.class)))
            ClienteDto clienteDTORequest) throws URISyntaxException {
        ClienteDto nuevoCliente = clienteServicio.crearNuevoCliente(clienteDTORequest);
        if (nuevoCliente != null) {
            URI location = new URI("/api/clientes/" + nuevoCliente.getIdentificacion());
            return ResponseEntity.created(location).body(nuevoCliente);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Actualizar un cliente existente")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente actualizado con éxito"),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado")
    })
    @PutMapping
    public ResponseEntity<ClienteDto> actualizarCliente(
            @RequestBody(description = "Detalles del cliente a actualizar", required = true, content = @Content(schema = @Schema(implementation = ClienteDto.class)))
            ClienteDto clienteDTORequest) {
        ClienteDto clienteActualizado = clienteServicio.actualizarCliente(clienteDTORequest);
        if (clienteActualizado != null) {
            return ResponseEntity.ok(clienteActualizado);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Eliminar un cliente por identificación")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente eliminado con éxito"),
            @ApiResponse(responseCode = "204", description = "Cliente no encontrado")
    })
    @DeleteMapping("/{identificacion}")
    public ResponseEntity<Void> eliminarCliente(
            @Parameter(description = "Identificación del cliente a eliminar", required = true)
            @PathVariable(name = "identificacion") String identificacion) {
        try {
            clienteServicio.eliminarCliente(identificacion);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.noContent().build();
        }
    }

    @Operation(summary = "Obtener un cliente por identificación")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente encontrado"),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado")
    })
    @GetMapping("/{identificacion}")
    public ResponseEntity<ClienteDto> obtenerCliente(
            @Parameter(description = "Identificación del cliente a obtener", required = true)
            @PathVariable(name = "identificacion") String identificacion) {
        ClienteDto cliente = clienteServicio.obtenerCliente(identificacion);
        if (cliente != null) {
            return ResponseEntity.ok(cliente);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Obtener un cliente completo por identificación")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Cliente encontrado"),
            @ApiResponse(responseCode = "404", description = "Cliente no encontrado")
    })
    @GetMapping("/full/{identificacion}")
    public ResponseEntity<ClienteDto> obtenerClienteFull(
            @Parameter(description = "Identificación del cliente a obtener", required = true)
            @PathVariable(name = "identificacion") String identificacion) {
        ClienteDto cliente = clienteServicio.obtenerClienteFull(identificacion);
        if (cliente != null) {
            return ResponseEntity.ok(cliente);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Obtener todos los clientes sin datos de cuenta")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Clientes encontrados"),
            @ApiResponse(responseCode = "204", description = "No hay clientes")
    })
    @GetMapping("/all")
    public ResponseEntity<List<ClienteDto>> obtenerTodosClientesSinDatosCuenta() {
        List<ClienteDto> clientes = clienteServicio.obtenerTodosClienteSinDatosCuenta();
        if (clientes != null && !clientes.isEmpty()) {
            return ResponseEntity.ok(clientes);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @Operation(summary = "Obtener todos los clientes con datos de cuenta")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Clientes encontrados"),
            @ApiResponse(responseCode = "204", description = "No hay clientes")
    })
    @GetMapping("/all/full")
    public ResponseEntity<List<ClienteDto>> obtenerTodosClientes() {
        List<ClienteDto> clientes = clienteServicio.obtenerTodosClienteDatosCuenta();
        if (clientes != null && !clientes.isEmpty()) {
            return ResponseEntity.ok(clientes);
        } else {
            return ResponseEntity.noContent().build();
        }
    }

    @Operation(summary = "Obtener cliente y reporte por parámetros")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Clientes encontrados"),
            @ApiResponse(responseCode = "204", description = "No hay clientes")
    })
    @GetMapping("/reporte")
    public ResponseEntity<List<ClienteDto>> obtenerClienteYReporte(
            @Parameter(description = "Identificación del cliente", required = true)
            @RequestParam(name = "identificacion") String identificacion,
            @Parameter(description = "Número de cuenta del cliente", required = true)
            @RequestParam(name = "numeroCuenta") String numeroCuenta,
            @Parameter(description = "Fecha inicial del reporte", required = true)
            @RequestParam(name = "fechaInicial") String fechaInicial,
            @Parameter(description = "Fecha final del reporte", required = true)
            @RequestParam(name = "fechaFinal") String fechaFinal) {

        List<ClienteDto> clientes = clienteServicio.obtenerTodosClienteDatosCuenta();
        if (clientes != null && !clientes.isEmpty()) {
            return ResponseEntity.ok(clientes);
        } else {
            return ResponseEntity.noContent().build();
        }
    }
}
