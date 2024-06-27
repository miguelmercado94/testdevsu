package com.devsu.micropersona.bd.entidad;



import jakarta.persistence.*;
import lombok.*;

@Entity
@PrimaryKeyJoinColumn(name = "cliente_id", referencedColumnName = "id")
@Data
@EqualsAndHashCode(callSuper = true)
public class Cliente extends Persona {

    @Transient
    private Long clienteId;

    private String contrasena;
    private String estado;

    public Long getClienteId() {
        return super.getId();
    }
}
