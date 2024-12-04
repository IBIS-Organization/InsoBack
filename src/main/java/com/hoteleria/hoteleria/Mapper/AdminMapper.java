package com.hoteleria.hoteleria.Mapper;

import com.hoteleria.hoteleria.DTO.AdminDTO;
import com.hoteleria.hoteleria.Entity.Admin;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public class AdminMapper {
    private final ModelMapper modelMapper;

    public AdminMapper(ModelMapper modelMapper){
        this.modelMapper=modelMapper;
    }

    public AdminDTO toDTO(Admin admin){
        return modelMapper.map(admin, AdminDTO.class);
    }

    public Admin toEntity(AdminDTO adminDTO){
        return modelMapper.map(adminDTO, Admin.class);
    }
}
