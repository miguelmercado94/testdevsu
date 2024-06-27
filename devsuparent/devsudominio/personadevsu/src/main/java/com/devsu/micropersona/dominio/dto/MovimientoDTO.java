package com.devsu.micropersona.dominio.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@Data
@EqualsAndHashCode(callSuper=false)
@ApiModel(description = "Detalles del Movimiento")
public class MovimientoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(notes = "ID único del movimiento", example = "1")
    private Long id;

    @ApiModelProperty(notes = "Fecha del movimiento", example = "2023-06-27T00:00:00.000Z", required = true)
    private Date fecha;

    @ApiModelProperty(notes = "Saldo después del movimiento", example = "1000.50", required = true)
    private BigDecimal saldo;

    @ApiModelProperty(notes = "Tipo de movimiento (CREDITO, DEBITO)", example = "CREDITO", required = true)
    private String tipoMovimiento;

    @ApiModelProperty(notes = "Valor del movimiento", example = "200.75", required = true)
    private BigDecimal valor;

    @ApiModelProperty(notes = "ID de la cuenta asociada al movimiento", example = "1", required = true)
    private Long cuentaId; 
}

