package com.hoteleria.hoteleria.Repository;

import com.hoteleria.hoteleria.Entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface ClienteRepository extends JpaRepository<Cliente, Long> {
    Optional<Cliente> findByEmail(String email);
    Optional<Cliente> findById(Long id);
    Optional<Cliente> findByFirstNameAndLastName(String firstName, String lastName);
}
