package com.hoteleria.hoteleria.Service.impl;

import com.hoteleria.hoteleria.Entity.Habitacion;
import com.hoteleria.hoteleria.Entity.Reserva;
import com.hoteleria.hoteleria.Repository.HabitacionRepository;
import com.hoteleria.hoteleria.Repository.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class HabitacionService {

    @Autowired
    private HabitacionRepository habitacionRepository;
    @Autowired
    private ReservaRepository reservaRepository;
    public List<Habitacion> getHabitacionesDisponibles(LocalDate fechaInicio, LocalDate fechaFin, Integer capacidad, String categoria) {
        List <Habitacion> habitacionesDisponibles = habitacionRepository.findByDisponibleTrueAndCapacidadGreaterThanEqualAndCategoria(capacidad, categoria);
        return habitacionesDisponibles.stream()
                .filter(habitacion -> {
                    List<Reserva> reservas = reservaRepository.findByHabitacion(habitacion);
                    return reservas.stream().noneMatch(reserva -> (reserva.getFechaInicio().isBefore(fechaFin)&& reserva.getFechaFin().isAfter(fechaInicio)));
                }).collect(Collectors.toList());
    }

    public Optional<Habitacion> getHabitacionById(Long id) {
        return habitacionRepository.findById(id);
    }

    public Habitacion saveHabitacion(Habitacion habitacion) {
        return habitacionRepository.save(habitacion);
    }

    public List<Habitacion> getHabitaciones() {return habitacionRepository.findAll();}
}
