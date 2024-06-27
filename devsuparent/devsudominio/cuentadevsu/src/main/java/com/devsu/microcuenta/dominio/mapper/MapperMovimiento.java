package com.devsu.microcuenta.dominio.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.devsu.microcuenta.bd.entidad.Cuenta;
import com.devsu.microcuenta.bd.entidad.Movimiento;
import com.devsu.microcuenta.bd.repositorio.CuentaRepositorio;
import com.devsu.microcuenta.dominio.dto.MovimientoDTO;

@Component
public class MapperMovimiento {
	
	@Autowired
    private  ModelMapper modelMapper;
    
	@Autowired
	private CuentaRepositorio repoCuenta;
    

    public MovimientoDTO convertToDto(Movimiento movimiento) {
        return modelMapper.map(movimiento, MovimientoDTO.class);
    }

    public Movimiento convertToEntity(MovimientoDTO movimientoDTO) {
        return modelMapper.map(movimientoDTO, Movimiento.class);
    }

    public Movimiento convertToEntityWithCuenta(MovimientoDTO movimientoDTO) {
        Movimiento movimiento = modelMapper.map(movimientoDTO, Movimiento.class);
        movimiento.setCuenta(repoCuenta.findById(movimientoDTO.getId()).get());
        return movimiento;
    }
}
