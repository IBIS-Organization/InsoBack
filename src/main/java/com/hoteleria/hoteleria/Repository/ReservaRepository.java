package com.hoteleria.hoteleria.Repository;

import com.hoteleria.hoteleria.Entity.Habitacion;
import com.hoteleria.hoteleria.Entity.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ReservaRepository extends JpaRepository<Reserva, Integer> {
    List<Reserva> findByHabitacion(Habitacion habitacion);

    List<Reserva> findByCliente_Id(Integer clienteId);
    Optional<Reserva> findById(Integer id);
    List<Reserva> findByEstado(String estado);
}
