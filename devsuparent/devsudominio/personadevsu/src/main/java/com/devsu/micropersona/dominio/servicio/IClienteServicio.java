package com.devsu.micropersona.dominio.servicio;

import java.util.Date;
import java.util.List;

import com.devsu.micropersona.dominio.dto.ClienteDto;

public interface IClienteServicio {

	ClienteDto crearNuevoCliente(ClienteDto cliente);
	
	ClienteDto actualizarCliente(ClienteDto cliente);
	
	void eliminarCliente(String identificacion) throws Exception;
	
	ClienteDto obtenerClienteFull(String identificacion);
	
	ClienteDto obtenerCliente(String identificacion);
	
	ClienteDto obtenerClienteYReporteCuenta(String identificacion, String nummeroCuenta,
			Date fechaInicial, Date fechaFinal);
	
	List<ClienteDto> obtenerTodosClienteSinDatosCuenta();

	List<ClienteDto> obtenerTodosClienteDatosCuenta();

	
}
