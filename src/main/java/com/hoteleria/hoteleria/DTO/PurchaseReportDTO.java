package com.hoteleria.hoteleria.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseReportDTO {
    private  Integer quantity;
    private String consultDate;
}
