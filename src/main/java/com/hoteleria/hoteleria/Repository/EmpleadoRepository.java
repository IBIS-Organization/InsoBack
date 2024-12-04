package com.hoteleria.hoteleria.Repository;

import com.hoteleria.hoteleria.Entity.Empleado;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EmpleadoRepository extends JpaRepository<Empleado, Long> {
    Optional<Empleado> findByEmail(String email);
    Optional<Empleado> findById(Long id);
    Optional<Empleado> findByFirstNameAndLastName(String firstName, String lastName);
}
