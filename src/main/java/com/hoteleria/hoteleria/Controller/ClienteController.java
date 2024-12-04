package com.hoteleria.hoteleria.Controller;

import com.hoteleria.hoteleria.DTO.ClienteDTO;
import com.hoteleria.hoteleria.Entity.Cliente;
import com.hoteleria.hoteleria.Service.impl.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;
@RestController
@RequestMapping("/clients")
public class ClienteController {
    @Autowired
    private ClienteService clienteService;

    @GetMapping("/listarestudiantes")
    public ResponseEntity<List<ClienteDTO>> listarClientes() {
        List<ClienteDTO> estudiantes = clienteService.listarTodosLosEstudiantes();
        return ResponseEntity.ok(estudiantes);
    }
    @GetMapping("/perfil")
    public ResponseEntity<?> obtenerPerfilCliente() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();


        String email = authentication.getName();

        Optional<Cliente> estudiante = clienteService.obtenerPerfilEstudiantePorEmail(email);



        if (estudiante.isPresent()) {
            return ResponseEntity.ok(estudiante.get());
        } else {
            return ResponseEntity.status(404).body("{\"error\": \"Perfil del Estudiante no encontrado.\"}");
        }
    }
}
