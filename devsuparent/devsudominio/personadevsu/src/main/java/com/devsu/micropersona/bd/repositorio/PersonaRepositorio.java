package com.devsu.micropersona.bd.repositorio;

import com.devsu.micropersona.bd.entidad.Persona;
import org.springframework.data.repository.CrudRepository;

public interface PersonaRepositorio extends CrudRepository<Persona, Long> {
}
