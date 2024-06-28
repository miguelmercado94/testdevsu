package com.devsu.micropersona.dominio.controller;

import com.devsu.micropersona.dominio.dto.ClienteDto;
import com.devsu.micropersona.dominio.servicio.IClienteServicio;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import io.swagger.annotations.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

@RestController
@RequestMapping("/api/clientes/v1")
@Api(value = "Sistema de Gestión de Clientes", description = "Operaciones relacionadas con la gestión de clientes")
public class ClienteController {

    @Autowired
    private IClienteServicio clienteServicio;

    @ApiOperation(value = "Crear un nuevo cliente")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Cliente creado con éxito"),
            @ApiResponse(code = 400, message = "Solicitud incorrecta")
    })
    @PostMapping
    public ResponseEntity<ClienteDto> crearNuevoCliente(
            @ApiParam(value = "Detalles del cliente a crear", required = true)
            @RequestBody ClienteDto clienteDTORequest) throws URISyntaxException {
        ClienteDto nuevoCliente = clienteServicio.crearNuevoCliente(clienteDTORequest);
        if (nuevoCliente != null) {
            URI location = new URI("/api/clientes/" + nuevoCliente.getIdentificacion());
            return ResponseEntity.created(location).body(nuevoCliente);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @ApiOperation(value = "Actualizar un cliente existente")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Cliente actualizado con éxito"),
            @ApiResponse(code = 404, message = "Cliente no encontrado")
    })
    @PutMapping
    public ResponseEntity<ClienteDto> actualizarCliente(
            @ApiParam(value = "Detalles del cliente a actualizar", required = true)
            @RequestBody ClienteDto clienteDTORequest) {
        ClienteDto clienteActualizado = clienteServicio.actualizarCliente(clienteDTORequest);
        if (clienteActualizado != null) {
            return ResponseEntity.ok(clienteActualizado);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @ApiOperation(value = "Eliminar un cliente por identificación")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Cliente eliminado con éxito"),
            @ApiResponse(code = 204, message = "Cliente no encontrado")
    })
    @DeleteMapping("/{identificacion}")
    public ResponseEntity<Void> eliminarCliente(
            @ApiParam(value = "Identificación del cliente a eliminar", required = true)
            @PathVariable(name = "identificacion") String identificacion) {
        try {
            clienteServicio.eliminarCliente(identificacion);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return ResponseEntity.noContent().build();
        }
    }

    @ApiOperation(value = "Obtener un cliente por identificación")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Cliente encontrado"),
            @ApiResponse(code = 404, message = "Cliente no encontrado")
    })
    @GetMapping("/{identificacion}")
    public ResponseEntity<ClienteDto> obtenerCliente(
            @ApiParam(value = "Identificación del cliente a obtener", required = true)
            @PathVariable(name = "identificacion") String identificacion) {
        ClienteDto cliente = clienteServicio.obtenerCliente(identificacion);
        if (cliente != null) {
            return ResponseEntity.ok(cliente);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    
    @ApiOperation(value = "Obtener un cliente completo por identificación")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Cliente encontrado"),
            @ApiResponse(code = 404, message = "Cliente no encontrado")
    })
    @GetMapping("/full/{identificacion}")
    public ResponseEntity<ClienteDto> obtenerClienteFull(
            @ApiParam(value = "Identificación del cliente a obtener", required = true)
            @PathVariable(name = "identificacion") String identificacion) {
        ClienteDto cliente = clienteServicio.obtenerClienteFull(identificacion);
        if (cliente != null) {
            return ResponseEntity.ok(cliente);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @ApiOperation(value = "Obtener todos los clientes sin datos de cuenta")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Clientes encontrados"),
            @ApiResponse(code = 204, message = "No hay clientes")
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
    
    @ApiOperation(value = "Obtener todos los clientes con datos de cuenta")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Clientes encontrados"),
            @ApiResponse(code = 204, message = "No hay clientes")
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
    
    @ApiOperation(value = "Obtener cliente y reporte por parámetros")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Clientes encontrados"),
            @ApiResponse(code = 204, message = "No hay clientes")
    })
    @GetMapping("/reporte")
    public ResponseEntity<List<ClienteDto>> obtenerClienteYReporte(
            @ApiParam(value = "Identificación del cliente", required = true)
            @RequestParam(name = "identificacion") String identificacion,
            @ApiParam(value = "Número de cuenta del cliente", required = true)
            @RequestParam(name = "numeroCuenta") String numeroCuenta,
            @ApiParam(value = "Fecha inicial del reporte", required = true)
            @RequestParam(name = "fechaInicial") String fechaInicial,
            @ApiParam(value = "Fecha final del reporte", required = true)
            @RequestParam(name = "fechaFinal") String fechaFinal) {
        
        List<ClienteDto> clientes = clienteServicio.obtenerTodosClienteDatosCuenta();
        if (clientes != null && !clientes.isEmpty()) {
            return ResponseEntity.ok(clientes);
        } else {
            return ResponseEntity.noContent().build();
        }
    }
}
