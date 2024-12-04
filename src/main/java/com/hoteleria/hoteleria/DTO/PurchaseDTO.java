package com.hoteleria.hoteleria.DTO;

import com.hoteleria.hoteleria.Entity.enums.PaymentStatus;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class PurchaseDTO {
    private Integer id;
    private Float total;
    private LocalDateTime createAt;
    private PaymentStatus paymentStatus;
    private String user;
    private List<PurchaseItemDTO> items;
}

