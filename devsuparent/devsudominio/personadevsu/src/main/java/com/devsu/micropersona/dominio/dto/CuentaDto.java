package com.devsu.micropersona.dominio.dto;

import lombok.Data;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.Parameter;

@Data
@Schema(description = "Detalles de la Cuenta")
public class CuentaDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Schema(description = "ID único de la cuenta", example = "1")
    private Long id;

    @Schema(description = "Número de cuenta único", example = "1234567890")
    private String numeroCuenta;

    @Schema(description = "Tipo de cuenta (AHORROS, CORRIENTE)", example = "AHORROS")
    private String tipoCuenta;

    @Schema(description = "Saldo inicial de la cuenta", example = "1000.50")
    private BigDecimal saldoInicial;

    @Schema(description = "Estado de la cuenta (ACTIVA, INACTIVA)", example = "ACTIVA")
    private String estado;

    @Schema(description = "Identificación del cliente", example = "9876543210")
    private String cliente;

    @Schema(description = "Lista de movimientos asociados a la cuenta")
    private List<MovimientoDTO> movimientos;
}
