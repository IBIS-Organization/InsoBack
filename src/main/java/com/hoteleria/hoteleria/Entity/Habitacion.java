package com.hoteleria.hoteleria.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class Habitacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String categoria;
    private Integer capacidad;
    private String descripcion;
    private String img;
    private Double precio;
    private Boolean disponible;
    private Integer cantidadTotal;      // Total de habitaciones de este tipo
    private Integer cantidadReservada;
    @ManyToOne
    @JoinColumn(name = "hotel_id")
    @JsonIgnore
    private Hotel hotel;
}