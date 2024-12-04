package com.hoteleria.hoteleria.Entity;

import jakarta.persistence.Entity;
import lombok.Data;

@Data
@Entity
public class Admin extends User{
   private boolean needsPasswordChange = true;
}
