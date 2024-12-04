package com.hoteleria.hoteleria.Repository;

import com.hoteleria.hoteleria.Entity.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HotelRepository extends JpaRepository<Hotel, Long> {
}
