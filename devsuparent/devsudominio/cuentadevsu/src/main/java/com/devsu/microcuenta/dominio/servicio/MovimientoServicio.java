package com.devsu.microcuenta.dominio.servicio;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.devsu.microcuenta.bd.entidad.Cuenta;
import com.devsu.microcuenta.bd.entidad.Movimiento;
import com.devsu.microcuenta.bd.repositorio.CuentaRepositorio;
import com.devsu.microcuenta.bd.repositorio.MovimientoRepositorio;
import com.devsu.microcuenta.dominio.dto.MovimientoDTO;
import com.devsu.microcuenta.dominio.mapper.MapperMovimiento;
import com.devsu.microcuenta.dominio.util.TipoMovimiento;

@Service
public class MovimientoServicio implements IMovimientoServicio {
	
	@Autowired
	private MovimientoRepositorio movimientoRepositorio;
	
	@Autowired
	private CuentaRepositorio cuentaRepositorio;
	
	@Autowired
	private MapperMovimiento mapperMovimiento;
	

	@Override
	public MovimientoDTO agregarNuevoMovimiento(MovimientoDTO dto) {
		
		List<MovimientoDTO> movimientos = obtenerMovimientosPorCuenta(dto.getCuentaId());
		
		Cuenta cuentaAux = cuentaRepositorio.findById(dto.getCuentaId()).get();
		
		MovimientoDTO reponse = (MovimientoDTO) dto;
		
		reponse.setFecha(new Date());
		
		BigDecimal saldoActual = movimientos.isEmpty() ? cuentaAux.getSaldoInicial() : movimientos.get(movimientos.size() - 1).getSaldo();
	        
	        if (TipoMovimiento.CREDITO.toString().equals(reponse.getTipoMovimiento())) {
	            saldoActual = saldoActual.add(reponse.getValor());
	        } else if (TipoMovimiento.DEBITO.toString().equals(reponse.getTipoMovimiento())) {
	            saldoActual = saldoActual.subtract(reponse.getValor());
	        }

	        reponse.setSaldo(saldoActual);
	        return  mapperMovimiento.convertToDto(movimientoRepositorio.save(mapperMovimiento.convertToEntity(reponse)));
	        
	}

	

	@Override
	public MovimientoDTO obtenerMovimiento(Long id) {
		// TODO Auto-generated method stub
		return mapperMovimiento.convertToDto(movimientoRepositorio.findById(id).get());
	}

	@Override
	public List<MovimientoDTO> obtenerMovimientosPorCuenta(Long idCuenta) {
		
		Cuenta cuentaAux = cuentaRepositorio.findById(idCuenta).get();
		
		return cuentaAux.getMovimientos().stream()
        .map(mapperMovimiento::convertToDto)
        .collect(Collectors.toList());
		
	}
	
	@Override
	public List<MovimientoDTO> obtenerMovimientosPorNumeroCuenta(String numeroCuenta) {
		
		Cuenta cuentaAux = cuentaRepositorio.findByNumeroCuenta(numeroCuenta);
		
		return cuentaAux.getMovimientos().stream()
        .map(mapperMovimiento::convertToDto)
        .collect(Collectors.toList());
		
	}



	@Override
	public List<MovimientoDTO> obtenerMovimientosPorCuentaYFecha(String numeroCuenta, Date fechaInicial,
			Date fechaFinal) {
	List<Movimiento> movimientos= movimientoRepositorio.findByCuentaNumeroCuentaAndFechaBetween(numeroCuenta, fechaInicial, fechaFinal);
		
		return movimientos.stream()
        .map(mapperMovimiento::convertToDto)
        .collect(Collectors.toList());
	}

}
