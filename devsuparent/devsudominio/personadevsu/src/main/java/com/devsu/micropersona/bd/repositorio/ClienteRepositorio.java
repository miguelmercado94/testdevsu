package com.devsu.micropersona.bd.repositorio;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.devsu.micropersona.bd.entidad.Cliente;


public interface ClienteRepositorio extends CrudRepository<Cliente, Long> {
	
	Optional<Cliente> findByIdentificacion(String identificacion);

}
