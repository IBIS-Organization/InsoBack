package com.hoteleria.hoteleria.Entity;

import com.hoteleria.hoteleria.Config.ReservaListener;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@EntityListeners(ReservaListener.class)
public class Reserva {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    @ManyToOne
    @JoinColumn(name = "habitacion_id")
    private Habitacion habitacion;
    @ManyToOne
    private Cliente cliente;
    @Column(name = "dni_cliente")
    private String dniCliente;
    private String nombreCliente;
    private String estado; // Estado de la reserva: 'pendiente', 'check-in', 'check-out'
    private LocalDateTime fechaCheckIn; // Fecha y hora de check-in
    private LocalDateTime fechaCheckOut; // Fecha y hora de check-out

    public boolean isExpired(){
        return LocalDate.now().isAfter(fechaFin);
    }
}
