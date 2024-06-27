package com.devsu.micropersona.dominio.mapper;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.devsu.micropersona.bd.entidad.Cliente;
import com.devsu.micropersona.dominio.dto.ClienteDto;

@Component
public class ClienteMapper {

    private final ModelMapper modelMapper;

    @Autowired
    public ClienteMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
        this.modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
    }

    public ClienteDto toClienteDTORequest(Cliente cliente) {
        return modelMapper.map(cliente, ClienteDto.class);
    }

    public Cliente toCliente(ClienteDto dto) {
        return modelMapper.map(dto, Cliente.class);
    }

    public List<ClienteDto> toDtoList(Iterable<Cliente> clientes) {
        return StreamSupport.stream(clientes.spliterator(), false)
                .map(this::toClienteDTORequest)
                .collect(Collectors.toList());
    }

}

