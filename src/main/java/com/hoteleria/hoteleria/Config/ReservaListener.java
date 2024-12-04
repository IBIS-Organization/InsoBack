package com.hoteleria.hoteleria.Config;

import com.hoteleria.hoteleria.Entity.Reserva;

import javax.persistence.PostLoad;
import javax.persistence.PostUpdate;

public class ReservaListener {

    @PostLoad
    @PostUpdate
    public void checkReservaExpiration(Reserva reserva) {
        if (reserva.isExpired() && !reserva.getHabitacion().getDisponible()) {
            reserva.getHabitacion().setDisponible(true);
        }
    }
}
