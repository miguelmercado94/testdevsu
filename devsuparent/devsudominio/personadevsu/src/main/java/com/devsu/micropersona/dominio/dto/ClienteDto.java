package com.devsu.micropersona.dominio.dto;

import java.io.Serializable;
import java.util.List;
import com.devsu.micropersona.bd.entidad.Persona;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.Parameter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Detalles del Cliente")
public class ClienteDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "Nombre del cliente", example = "Juan Perez", required = true)
    private String nombre;

    @Schema(description = "Edad del cliente", example = "30", required = true)
    private Integer edad;

    @Schema(description = "Género del cliente (M, F)", example = "M", required = true)
    private Persona.Genero genero;

    @Schema(description = "Identificación única del cliente", example = "1234567890", required = true)
    private String identificacion;

    @Schema(description = "Dirección del cliente", example = "Calle 123 #45-67")
    private String direccion;

    @Schema(description = "Teléfono del cliente", example = "555-1234")
    private String telefono;

    @Schema(description = "Contraseña del cliente", example = "password123", required = true)
    private String contrasena;

    @Schema(description = "Estado del cliente (ACTIVO, INACTIVO)", example = "ACTIVO", required = true)
    private String estado;

    @Schema(description = "Lista de cuentas asociadas al cliente")
    private List<CuentaDto> cuentas;
}
