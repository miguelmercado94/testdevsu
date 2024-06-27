package com.devsu.microcuenta.dominio.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.devsu.microcuenta.bd.entidad.Cuenta;
import com.devsu.microcuenta.dominio.dto.CuentaDto;

@Component
public class MapperCuenta {

    private  ModelMapper modelMapper;

    @Autowired
    public MapperCuenta(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public CuentaDto convertToCuentaDto(Cuenta cuenta) {
        return modelMapper.map(cuenta, CuentaDto.class);
    }

    public Cuenta convertToCuentaEntity(CuentaDto cuentaDTO) {
        return modelMapper.map(cuentaDTO, Cuenta.class);
    }
}
