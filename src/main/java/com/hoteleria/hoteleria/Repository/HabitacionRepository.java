package com.hoteleria.hoteleria.Repository;

import com.hoteleria.hoteleria.Entity.Habitacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface HabitacionRepository extends JpaRepository<Habitacion, Long> {
    List<Habitacion> findByDisponible(boolean disponible);
    List<Habitacion> findByDisponibleTrueAndCapacidadGreaterThanEqualAndCategoria(int capacidad, String categoria);

    List<Habitacion> findByHotelId(Long hotelId);
    List<Habitacion> findByHotelIdAndDisponibleTrue(Long hotelId);
}
