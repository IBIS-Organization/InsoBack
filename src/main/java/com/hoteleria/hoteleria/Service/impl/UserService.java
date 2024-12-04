package com.hoteleria.hoteleria.Service.impl;

import com.hoteleria.hoteleria.DTO.UserDTO;
import com.hoteleria.hoteleria.Entity.User;
import com.hoteleria.hoteleria.Repository.UserRepository;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;


@Service
public class UserService {
    @Autowired
    private UserRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JavaMailSender mailSender;

    public void registrarUsuario(UserDTO usuarioDTO) {

        User user = new User();
        user.setFirstName(usuarioDTO.getFirstName());
        user.setLastName(usuarioDTO.getLastName());
        user.setEmail(usuarioDTO.getEmail());
        user.setPassword(usuarioDTO.getPassword());


        user.setPassword(passwordEncoder.encode(usuarioDTO.getPassword()));


        usuarioRepository.save(user);
    }
    public User login(String email, String password) {
        User usuario = usuarioRepository.findByEmail(email);
        if (usuario != null && passwordEncoder.matches(password, usuario.getPassword())) {
            return usuario;
        }
        return null;
    }

    public void deleteUser(Integer id) throws Exception {
        User user = usuarioRepository.findById(id)
                .orElseThrow(() -> new Exception("Usuario no encontrado con id: " + id));

        usuarioRepository.delete(user);
    }

    public User getUserById(Integer id) throws Exception {
        return usuarioRepository.findById(id)
                .orElseThrow(() -> new Exception("Usuario no encontrado con id: " + id));
    }

    public List<User> getAllUsers() {
        return usuarioRepository.findAll();
    }

    public void updateAndEncryptPassword(Integer id, String newPassword) throws Exception {
        User user = usuarioRepository.findById(id)
                .orElseThrow(() -> new Exception("Usuario no encontrado con id: " + id));


        String encryptedPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encryptedPassword);


        usuarioRepository.save(user);
    }

    public User findByEmail(String email) {
        return usuarioRepository.findByEmail(email);
    }

    public void generateResetPasswordToken(String email) throws Exception {
        User user = usuarioRepository.findByEmail(email);
        if (user == null) {
            throw new Exception("No se encontró un usuario con ese correo.");
        }

        String token = UUID.randomUUID().toString();
        user.setResetPasswordToken(token);
        usuarioRepository.save(user);


        sendResetPasswordEmail(user.getEmail(), token);
    }


    public void resetPassword(String token, String newPassword) throws Exception {
        User user = usuarioRepository.findByResetPasswordToken(token);
        if (user == null) {
            throw new Exception("Token inválido.");
        }

        user.setPassword(passwordEncoder.encode(newPassword));
        user.setResetPasswordToken(null);
        usuarioRepository.save(user);
    }

    private void imprimirtoken(String token){

    }
    private void sendResetPasswordEmail(String email, String token) throws Exception {
        String resetLink = "http://localhost:4200/confirmacion-contrasena?token=" + token;
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setTo(email);
        helper.setSubject("Restablecer tu contraseña en Hoteleria Ibis");
        helper.setText("<p>Hola,</p>" +
                "<p>Esperamos que estés teniendo un buen día. Hemos recibido una solicitud para restablecer la contraseña de tu cuenta en Ibis.</p>" +
                "<p>Si deseas restablecer tu contraseña, haz clic en el enlace de abajo:</p>" +
                "<a href=\"" + resetLink + "\">Restablecer contraseña</a>" +
                "<p>Si no solicitaste este cambio, puedes ignorar este correo.</p>" +
                "<p>¡Gracias por formar parte de nuestra comunidad!</p>" +
                "<p>Atentamente,<br>El equipo de desarrollo de Ibis</p>", true);

        mailSender.send(message);
    }

    public User updateUser(User user) {
        return usuarioRepository.save(user);
    }

}
