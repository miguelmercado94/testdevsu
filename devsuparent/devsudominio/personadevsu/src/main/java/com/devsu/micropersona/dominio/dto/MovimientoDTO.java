package com.devsu.micropersona.dominio.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import io.swagger.v3.oas.annotations.media.Schema;

@Data
@EqualsAndHashCode(callSuper=false)
@Schema(description = "Detalles del Movimiento")
public class MovimientoDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID único del movimiento", example = "1")
    private Long id;

    @Schema(description = "Fecha del movimiento", example = "2023-06-27T00:00:00.000Z", required = true)
    private Date fecha;

    @Schema(description = "Saldo después del movimiento", example = "1000.50", required = true)
    private BigDecimal saldo;

    @Schema(description = "Tipo de movimiento (CREDITO, DEBITO)", example = "CREDITO", required = true)
    private String tipoMovimiento;

    @Schema(description = "Valor del movimiento", example = "200.75", required = true)
    private BigDecimal valor;

    @Schema(description = "ID de la cuenta asociada al movimiento", example = "1", required = true)
    private Long cuentaId; 
}
