package com.devsu.micropersona.dominio.dto;

import java.io.Serializable;
import java.util.List;
import com.devsu.micropersona.bd.entidad.Persona;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "Detalles del Cliente")
public class ClienteDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(notes = "Nombre del cliente", example = "Juan Perez", required = true)
    private String nombre;

    @ApiModelProperty(notes = "Edad del cliente", example = "30", required = true)
    private Integer edad;

    @ApiModelProperty(notes = "Género del cliente (M, F)", example = "M", required = true)
    private Persona.Genero genero;

    @ApiModelProperty(notes = "Identificación única del cliente", example = "1234567890", required = true)
    private String identificacion;

    @ApiModelProperty(notes = "Dirección del cliente", example = "Calle 123 #45-67")
    private String direccion;

    @ApiModelProperty(notes = "Teléfono del cliente", example = "555-1234")
    private String telefono;

    @ApiModelProperty(notes = "Contraseña del cliente", example = "password123", required = true)
    private String contrasena;

    @ApiModelProperty(notes = "Estado del cliente (ACTIVO, INACTIVO)", example = "ACTIVO", required = true)
    private String estado;

    @ApiModelProperty(notes = "Lista de cuentas asociadas al cliente")
    private List<CuentaDto> cuentas;
}
