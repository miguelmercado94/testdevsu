package com.devsu.microcuenta.bd.repositorio;

import java.util.Date;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.devsu.microcuenta.bd.entidad.Movimiento;

@Repository
public interface MovimientoRepositorio extends CrudRepository<Movimiento, Long> {
	
    List<Movimiento> findByCuentaNumeroCuentaAndFechaBetween(String numeroCuenta, Date startDate, Date endDate);

	
}
