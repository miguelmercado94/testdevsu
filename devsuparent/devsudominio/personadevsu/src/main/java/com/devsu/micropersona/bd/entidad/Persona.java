package com.devsu.micropersona.bd.entidad;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Data
@EqualsAndHashCode
public class Persona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 1, max = 200)
    private String nombre;

    @NotNull
    private Integer edad;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Genero genero;

    @NotNull
    @Column(unique = true)
    @Size(min = 1, max = 30)
    private String identificacion;

    private String direccion;
    private String telefono;

   
    public enum Genero {
        M, F
    }
}
