package com.devsu.microcuenta.dominio.dto;

import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import com.devsu.microcuenta.dominio.util.TipoCuenta;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@Data
@ApiModel(description = "Detalles de la Cuenta")
public class CuentaDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(notes = "ID único de la cuenta", example = "1")
    private Long id;

    @ApiModelProperty(notes = "Número de cuenta único", example = "1234567890", required = true)
    private String numeroCuenta;

    @ApiModelProperty(notes = "Tipo de cuenta (AHORROS, CORRIENTE)", example = "AHORROS", required = true)
    private TipoCuenta tipoCuenta;

    @ApiModelProperty(notes = "Saldo inicial de la cuenta", example = "1000.50", required = true)
    private BigDecimal saldoInicial;

    @ApiModelProperty(notes = "Estado de la cuenta (ACTIVA, INACTIVA)", example = "ACTIVA", required = true)
    private String estado;

    @ApiModelProperty(notes = "Identificación del cliente", example = "9876543210", required = true)
    private String cliente;

    @ApiModelProperty(notes = "Lista de movimientos asociados a la cuenta")
    private List<MovimientoDTO> movimientos;
}
