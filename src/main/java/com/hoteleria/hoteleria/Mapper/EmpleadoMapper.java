package com.hoteleria.hoteleria.Mapper;

import com.hoteleria.hoteleria.DTO.EmpleadoDTO;
import com.hoteleria.hoteleria.Entity.Empleado;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class EmpleadoMapper {
    private final ModelMapper modelMapper;

    public EmpleadoMapper(ModelMapper modelMapper){
        this.modelMapper=modelMapper;
    }

    public EmpleadoDTO toDTO(Empleado empleado){
        return modelMapper.map(empleado, EmpleadoDTO.class);
    }

    public Empleado toEntity(EmpleadoDTO asesorDTO){
        return modelMapper.map(asesorDTO, Empleado.class);
    }
}
