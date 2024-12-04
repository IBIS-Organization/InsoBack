package com.hoteleria.hoteleria.DTO;

import lombok.Data;

@Data
public class PurchaseItemCreateDTO {
    private Integer reservaId;
    private Integer quantity;
    private Float price;
}

