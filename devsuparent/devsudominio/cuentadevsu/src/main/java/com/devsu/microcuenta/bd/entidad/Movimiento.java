package com.devsu.microcuenta.bd.entidad;

//import jakarta.persistence.*;
import lombok.Data;
import java.math.BigDecimal;
import java.util.Date;

import jakarta.persistence.*;

import com.devsu.microcuenta.dominio.util.TipoMovimiento;

@Entity
@Data
public class Movimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Date fecha;

    @Enumerated(EnumType.STRING)
    private TipoMovimiento tipoMovimiento;

    private BigDecimal valor;
    private BigDecimal saldo;

    @ManyToOne
    @JoinColumn(name = "cuenta_id")
    private Cuenta cuenta;

    // Otros atributos, constructores, getters y setters
}


