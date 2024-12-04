package com.hoteleria.hoteleria.Mapper;



import com.hoteleria.hoteleria.DTO.ClienteDTO;
import com.hoteleria.hoteleria.Entity.Cliente;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClienteMapper {

    private final ModelMapper modelMapper;

    public ClienteMapper(ModelMapper modelMapper){
        this.modelMapper=modelMapper;
    }

    public ClienteDTO toDTO(Cliente estudiante){
        return modelMapper.map(estudiante, ClienteDTO.class);
    }

    public Cliente toEntity(ClienteDTO estudianteDTO){
        return modelMapper.map(estudianteDTO, Cliente.class);
    }
}
