package com.hoteleria.hoteleria.Repository;

import com.hoteleria.hoteleria.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByEmail(String email);
    User findByResetPasswordToken(String token);
}
