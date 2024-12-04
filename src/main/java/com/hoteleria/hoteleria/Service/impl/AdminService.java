package com.hoteleria.hoteleria.Service.impl;


import com.hoteleria.hoteleria.DTO.AdminDTO;
import com.hoteleria.hoteleria.Entity.Admin;
import com.hoteleria.hoteleria.Entity.enums.ERole;
import com.hoteleria.hoteleria.Mapper.AdminMapper;
import com.hoteleria.hoteleria.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AdminMapper adminMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;

    public AdminDTO registerAdvisor(AdminDTO adminDTO) {
        Admin admin = adminMapper.toEntity(adminDTO);
        ERole eRole = ERole.ADMIN;
        admin.setFirstName(adminDTO.getFirstName());
        admin.setLastName(adminDTO.getLastName());
        admin.setEmail(adminDTO.getEmail());
        admin.setPassword(passwordEncoder.encode(adminDTO.getPassword()));
        admin.setRole(eRole);
        admin = userRepository.save(admin);
        return adminMapper.toDTO(admin);
    }


}
