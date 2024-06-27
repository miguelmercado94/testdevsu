package com.devsu.microcuenta.bd.repositorio;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.devsu.microcuenta.bd.entidad.Cuenta;

@Repository
public interface CuentaRepositorio extends CrudRepository<Cuenta, Long> {
	
	Cuenta findByNumeroCuenta(String numeroCuenta);
	
	List<Cuenta> findByCliente(String cliente);
	
}

