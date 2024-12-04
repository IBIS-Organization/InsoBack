package com.hoteleria.hoteleria.Service.impl;


import com.hoteleria.hoteleria.DTO.EmpleadoDTO;
import com.hoteleria.hoteleria.Entity.Empleado;
import com.hoteleria.hoteleria.Entity.enums.ERole;
import com.hoteleria.hoteleria.Exceptions.BadRequestException;
import com.hoteleria.hoteleria.Mapper.EmpleadoMapper;
import com.hoteleria.hoteleria.Repository.EmpleadoRepository;
import com.hoteleria.hoteleria.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmpleadoService {
    @Autowired
    private EmpleadoRepository empleadoRepository;

    @Autowired private UserRepository usuarioRepository;

    @Autowired private PasswordEncoder passwordEncoder;

    @Autowired
    private EmpleadoMapper empleadoMapper;

    public Optional<Empleado> obtenerPerfilAsesorPorEmail(String email) {
        return empleadoRepository.findByEmail(email);
    }


    public List<Empleado> listarTodosLosAsesores() {
        return empleadoRepository.findAll();
    }

    public EmpleadoDTO registrarAsesor(EmpleadoDTO empleadoDTO) {
        empleadoRepository.findByFirstNameAndLastName(empleadoDTO.getFirstName(), empleadoDTO.getLastName())
                .ifPresent(existingAsesor -> {
                    throw new BadRequestException("El asesor ya existe con el mismo nombre y apellido");
                });
        Empleado empleado = empleadoMapper.toEntity(empleadoDTO);
        ERole eRole = ERole.EMPLEADO;

        empleado.setFirstName(empleadoDTO.getFirstName());
        empleado.setLastName(empleadoDTO.getLastName());
        empleado.setEmail(empleadoDTO.getEmail());
        empleado.setPassword(passwordEncoder.encode(empleadoDTO.getPassword())); // Asegúrate de que estés utilizando un codificador de contraseñas
        empleado.setRole(eRole);
        empleado = usuarioRepository.save(empleado);
        return empleadoMapper.toDTO(empleado);
    }

}
