package com.devsu.microcuenta.bd.entidad;

import java.math.BigDecimal;
import java.util.List;


import com.devsu.microcuenta.dominio.util.TipoCuenta;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Cuenta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String numeroCuenta;

    @Enumerated(EnumType.STRING)
    private TipoCuenta tipoCuenta;

    private BigDecimal saldoInicial;
    private String estado;

    @OneToMany(mappedBy = "cuenta", cascade = CascadeType.ALL)
    @JsonIgnoreProperties("cuenta")
    private List<Movimiento> movimientos;

    private String cliente; // Identificación del cliente

    // Otros atributos, constructores, getters y setters
}
