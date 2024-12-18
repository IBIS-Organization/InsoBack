package com.hoteleria.hoteleria.Service.impl;

import com.hoteleria.hoteleria.DTO.PurchaseCreateDTO;
import com.hoteleria.hoteleria.DTO.PurchaseDTO;
import com.hoteleria.hoteleria.DTO.PurchaseReportDTO;

import java.util.List;

public interface PurchaseService {
    PurchaseDTO createPurchase(PurchaseCreateDTO purchase);
    List<PurchaseDTO> getPurchaseHistoryByUserId(Integer userId);
    List<PurchaseReportDTO> getPurchaseReportByDate();

    List<PurchaseDTO> getPaidPurchasesByUserId(Integer userId);
    List<PurchaseDTO> getAllPurchases();
    PurchaseDTO confirmPurchase(Integer purchaseId);
    PurchaseDTO getPurchaseById(Integer purchaseId);
}
