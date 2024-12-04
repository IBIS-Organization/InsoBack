package com.hoteleria.hoteleria.Controller;

import com.hoteleria.hoteleria.Entity.Empleado;
import com.hoteleria.hoteleria.Service.impl.EmpleadoService;
import com.hoteleria.hoteleria.Service.impl.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/empleados")
public class EmpleadoController {
    @Autowired
    private EmpleadoService empleadoService;
    @Autowired
    private UserService userService;

    @GetMapping("/perfil")
    public ResponseEntity<?> obtenerPerfilAsesor() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();


        String email = authentication.getName();


        Optional<Empleado> asesor = empleadoService.obtenerPerfilAsesorPorEmail(email);


        if (asesor.isPresent()) {
            return ResponseEntity.ok(asesor.get());
        } else {
            return ResponseEntity.status(404).body("{\"error\": \"Perfil del asesor no encontrado.\"}");
        }
    }

    @GetMapping("/listarempleados")
    public ResponseEntity<List<Empleado>> listarEmpleados() {
        List<Empleado> asesores = empleadoService.listarTodosLosAsesores();
        return ResponseEntity.ok(asesores);
    }


    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Map<String, String>> deleteUser(@PathVariable Integer id) {
        try {
            userService.deleteUser(id);
            return ResponseEntity.ok(Map.of("mensaje", "Usuario eliminado correctamente") );
        } catch (Exception e) {
            return ResponseEntity.ok(Map.of("mensaje", "Error") );
        }
    }
}
