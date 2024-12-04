package com.hoteleria.hoteleria.DTO;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Data
public class ReservaDTO {
    private Integer id;
    private Long habitacionId;
    private Long clienteId;
    private String dniCliente;
    private String nombreCliente;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private String estado; // Estado de la reserva: 'pendiente', 'check-in', 'check-out'
    private LocalDateTime fechaCheckIn; // Fecha y hora de check-in
    private LocalDateTime fechaCheckOut; // Fecha y hora de check-out
}
