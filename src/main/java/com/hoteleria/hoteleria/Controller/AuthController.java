package com.hoteleria.hoteleria.Controller;

import com.hoteleria.hoteleria.DTO.*;
import com.hoteleria.hoteleria.Entity.Admin;
import com.hoteleria.hoteleria.Entity.Cliente;
import com.hoteleria.hoteleria.Entity.Empleado;
import com.hoteleria.hoteleria.Entity.User;
import com.hoteleria.hoteleria.Repository.AdminRepository;
import com.hoteleria.hoteleria.Repository.ClienteRepository;
import com.hoteleria.hoteleria.Repository.EmpleadoRepository;
import com.hoteleria.hoteleria.Service.impl.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;


@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService usuarioService;

    @Autowired
    private JwtUtilService jwtUtilService;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private EmpleadoService empleadoService;
    @Autowired
    private AdminService adminService;
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private EmpleadoRepository empleadoRepository;
    @Autowired
    private ClienteRepository clienteRepository;

    @PostMapping("/register")
    public ResponseEntity<?> registrarUsuario(@Validated @RequestBody UserDTO usuarioDTO) {
        try {
            usuarioService.registrarUsuario(usuarioDTO);
            return ResponseEntity.ok().body("{\"message\": \"Usuario registrado con éxito.\"}");
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\": \"Error al registrar el usuario.\"}");
        }
    }

    @PostMapping("/registerclient")
    public ResponseEntity<?> registrarCliente(@Validated @RequestBody ClienteDTO clienteDTO) {
        try {
            clienteService.registrarCliente(clienteDTO);
            return ResponseEntity.ok().body("{\"message\": \"Usuario registrado con éxito.\"}");
        } catch (Exception e) {
            e.printStackTrace();  // Imprimir el stack trace para más detalles
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\": \"Error al registrar el usuario: " + e.getMessage() + "\"}");
        }
    }

    @PostMapping("/registerempleado")
    public ResponseEntity<?> registrarEmpleado(@Validated @RequestBody EmpleadoDTO empleadoDTO) {
        try {
            empleadoService.registrarAsesor(empleadoDTO);
            return ResponseEntity.ok().body("{\"message\": \"Usuario registrado con éxito.\"}");
        } catch (Exception e) {
            e.printStackTrace();  // Imprimir el stack trace para más detalles
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("{\"error\":\"Error al registrar el usuario:" + e.getMessage() + "\"}");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, Object>> login(@RequestParam String email, @RequestParam String password) {
        Map<String, Object> response = new HashMap<>();

        Optional<Cliente> optionalStudent = clienteRepository.findByEmail(email);
        if (optionalStudent.isPresent()) {
            Cliente cliente = optionalStudent.get();


            if (!passwordEncoder.matches(password, cliente.getPassword())) {
                response.put("message", "Contraseña incorrecta");
                return ResponseEntity.badRequest().body(response);
            }

            String token = jwtUtilService.generateToken(cliente);
            response.put("message", "Inicio de sesión exitoso");
            response.put("token", token);
            return ResponseEntity.ok(response);
        }

        Optional<Empleado> optionalEmpleado = empleadoRepository.findByEmail(email);
        if (optionalEmpleado.isPresent()) {
            Empleado empleado = optionalEmpleado.get();

            if (!passwordEncoder.matches(password, empleado.getPassword())) {
                response.put("message", "Contraseña incorrecta");
                return ResponseEntity.badRequest().body(response);
            }


            String token = jwtUtilService.generateToken(empleado);

            response.put("message", "Inicio de sesión exitoso");
            response.put("token", token);


            response.put("requiresPasswordChange", empleado.isNeedsPasswordChange());

            return ResponseEntity.ok(response);
        }

        Optional<Admin> optionalAdmin = adminRepository.findByEmail(email);
        if (optionalAdmin.isPresent()) {
            Admin admin = optionalAdmin.get();

            if (!passwordEncoder.matches(password, admin.getPassword())) {
                response.put("message", "Contraseña incorrecta");
                return ResponseEntity.badRequest().body(response);
            }


            String token = jwtUtilService.generateToken(admin);

            response.put("message", "Inicio de sesión exitoso");
            response.put("token", token);


            response.put("requiresPasswordChange", admin.isNeedsPasswordChange());

            return ResponseEntity.ok(response);
        }

        response.put("message", "Correo electrónico no registrado");
        return ResponseEntity.badRequest().body(response);
    }


    @PutMapping("/update")
    public ResponseEntity<User> updateUserInfo(@RequestHeader("Authorization") String token,
                                               @RequestBody UserUpdateDTO userUpdateDto) {

        String jwt = token.substring(7);
        String email = jwtUtilService.extractUsername(jwt);


        User usuario = usuarioService.findByEmail(email);
        if (usuario != null) {

            usuario.setFirstName(userUpdateDto.getFirstName());
            usuario.setLastName(userUpdateDto.getLastName());
            usuario.setEmail(userUpdateDto.getEmail());

            User updatedUser = usuarioService.updateUser(usuario);

            return ResponseEntity.ok(updatedUser);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Integer id) {
        try {
            usuarioService.deleteUser(id);
            return ResponseEntity.ok("Usuario eliminado correctamente");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al eliminar el usuario: " + e.getMessage());
        }
    }

    @GetMapping("/obtener/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Integer id) {
        try {
            User user = usuarioService.getUserById(id);
            return ResponseEntity.ok(user);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al obtener el usuario: " + e.getMessage());
        }
    }

    @GetMapping("/obtenertodos")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = usuarioService.getAllUsers();
        return ResponseEntity.ok(users);
    }

    @PutMapping("/{id}/encrypt-password")
    public ResponseEntity<?> updatePassword(@PathVariable Integer id, @RequestParam String newPassword) {
        try {
            usuarioService.updateAndEncryptPassword(id, newPassword);
            return ResponseEntity.ok("Contraseña actualizada y encriptada exitosamente");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al actualizar la contraseña: " + e.getMessage());
        }
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestParam String email) {
        try {
            usuarioService.generateResetPasswordToken(email);

            // Devuelve una respuesta con encabezado de tipo JSON
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_TYPE, "application/json")
                    .body(Map.of("message", "Se ha enviado un correo con instrucciones para restablecer su contraseña."));
        } catch (Exception e) {
            // Devuelve una respuesta con encabezado de tipo JSON en caso de error
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .header(HttpHeaders.CONTENT_TYPE, "application/json")
                    .body(Map.of("error", "Error al generar el token de recuperación: " + e.getMessage()));
        }
    }


    @PostMapping("/registerAdmin")
    public void registerAdmin(@Validated @RequestBody AdminDTO adminDTO) {
        adminService.registerAdvisor(adminDTO);
        ResponseEntity.ok().body("{\"message\": \"Usuario registrado con éxito.\"}");
    }


    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestParam String token, @RequestParam String newPassword) {
        try {
            usuarioService.resetPassword(token, newPassword);
            // Devolver un JSON con un mensaje de éxito
            return ResponseEntity.ok(Map.of("message", "Contraseña actualizada exitosamente."));
        } catch (Exception e) {
            e.printStackTrace(); // Log del error para depuración
            // Devolver un JSON con el mensaje de error
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Error al restablecer la contraseña: " + e.getMessage()));
        }
    }


    @PostMapping("/change-password-admin")
    public ResponseEntity<Map<String, Object>> changePasswordAdmin(@RequestParam String email, @RequestParam String newPassword) {
        Map<String, Object> response = new HashMap<>();

        Optional<Admin> optionalAdmin = adminRepository.findByEmail(email);
        if (optionalAdmin.isPresent()) {
            Admin admin = optionalAdmin.get();

            // Actualiza la contraseña
            admin.setPassword(passwordEncoder.encode(newPassword));
            admin.setNeedsPasswordChange(false);
            adminRepository.save(admin);

            response.put("message", "Contraseña cambiada exitosamente");
            return ResponseEntity.ok(response);
        }

        response.put("message", "Admin no encontrado");
        return ResponseEntity.badRequest().body(response);
    }

    @PostMapping("/change-password-empleado")
    public ResponseEntity<Map<String, Object>> changePasswordEmpleado(@RequestParam String email, @RequestParam String newPassword) {
        Map<String, Object> response = new HashMap<>();

        Optional<Empleado> optionalAdmin = empleadoRepository.findByEmail(email);
        if (optionalAdmin.isPresent()) {
            Empleado admin = optionalAdmin.get();

            // Actualiza la contraseña
            admin.setPassword(passwordEncoder.encode(newPassword));
            admin.setNeedsPasswordChange(false);
            empleadoRepository.save(admin);

            response.put("message", "Contraseña cambiada exitosamente");
            return ResponseEntity.ok(response);
        }

        response.put("message", "Empleado no encontrado");
        return ResponseEntity.badRequest().body(response);
    }


    @GetMapping("/me")
    public ResponseEntity<User> getUserInfo(@RequestHeader("Authorization") String token) {

        String jwt = token.substring(7);


        String email = jwtUtilService.extractUsername(jwt);

        User usuario = usuarioService.findByEmail(email);

        if (usuario != null) {
            return ResponseEntity.ok(usuario);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

}

