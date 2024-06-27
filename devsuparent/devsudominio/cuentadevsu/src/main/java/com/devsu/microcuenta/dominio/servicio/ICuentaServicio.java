package com.devsu.microcuenta.dominio.servicio;

import java.util.Date;
import java.util.List;

import com.devsu.microcuenta.dominio.dto.CuentaDto;

public interface ICuentaServicio {
	
	 CuentaDto agregarNuevaCuenta(CuentaDto cuenta);
	 
	 CuentaDto actualizarCuenta(CuentaDto cuenta);
	 
	 CuentaDto obtenerCuentaPorNumero(String numeroCuenta);
	 
	 List<CuentaDto> obtenerCuentasPorCliente(String identificacion);
	 	 
	 List<CuentaDto> obtenerTodaslasCuentas();
	 
	 void eliminarCuenta(String numeroCuenta);
	 
	 CuentaDto reporteMovimientosCuentaPorRangoFecha(String numeroCuenta, Date fechaInicial, Date fechaFinal);

}
