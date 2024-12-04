package com.hoteleria.hoteleria.Entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "Empleados")
@Data
public class Empleado extends User{
    private boolean needsPasswordChange = true;
}
