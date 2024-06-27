package com.devsu.microcuenta.dominio.servicio;

import java.util.Date;
import java.util.List;

import com.devsu.microcuenta.dominio.dto.MovimientoDTO;

public interface IMovimientoServicio {
	
	MovimientoDTO agregarNuevoMovimiento(MovimientoDTO dto);
	
	MovimientoDTO obtenerMovimiento(Long id);
	
	List<MovimientoDTO> obtenerMovimientosPorCuenta(Long idCuenta);
	
	List<MovimientoDTO> obtenerMovimientosPorNumeroCuenta(String numeroCuenta);

	List<MovimientoDTO> obtenerMovimientosPorCuentaYFecha(String numeroCuenta,Date fechaInicial, Date fechaFinal);

}
