package com.hoteleria.hoteleria.Service.impl;

import com.hoteleria.hoteleria.DTO.ClienteDTO;
import com.hoteleria.hoteleria.DTO.ReniecResponseDTO;
import com.hoteleria.hoteleria.Entity.Cliente;
import com.hoteleria.hoteleria.Entity.enums.ERole;
import com.hoteleria.hoteleria.Exceptions.BadRequestException;
import com.hoteleria.hoteleria.Exceptions.ResourceNotFoundException;
import com.hoteleria.hoteleria.Mapper.ClienteMapper;
import com.hoteleria.hoteleria.Repository.ClienteRepository;
import com.hoteleria.hoteleria.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    @Autowired
    private UserRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ClienteMapper clienteMapper;


    public List<ClienteDTO> listarTodosLosEstudiantes() {
        List<Cliente> estudiantes = clienteRepository.findAll();
        return estudiantes.stream()
                .map(clienteMapper::toDTO)
                .toList();
    }


    public Optional<Cliente> obtenerPerfilEstudiantePorEmail(String email) {
        return clienteRepository.findByEmail(email);
    }

    public ClienteDTO findById(Long id){
        Cliente cliente = clienteRepository.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("El estudiante con ID " + id+ "no fue encontrado"));
        return clienteMapper.toDTO(cliente);
    }

    public ClienteDTO registrarCliente(ClienteDTO clienteDTO) {
        clienteRepository.findByFirstNameAndLastName(clienteDTO.getFirstName(), clienteDTO.getLastName())
                .ifPresent(existingAsesor -> {
                    throw new BadRequestException("El estudiante ya existe con el mismo nombre y apellido");
                });
        Cliente estudiante = clienteMapper.toEntity(clienteDTO);
        ERole eRole = ERole.CLIENTE;


        estudiante.setFirstName(clienteDTO.getFirstName());
        estudiante.setLastName(clienteDTO.getLastName());
        estudiante.setEmail(clienteDTO.getEmail());
        estudiante.setPassword(passwordEncoder.encode(clienteDTO.getPassword()));
        estudiante.setRole(eRole);
        estudiante = usuarioRepository.save(estudiante);
        return clienteMapper.toDTO(estudiante);
    }
}
