package com.hoteleria.hoteleria.Service.impl;

import com.hoteleria.hoteleria.DTO.PaymentCaptureResponse;
import com.hoteleria.hoteleria.DTO.PaymentOrderResponse;

public interface CheckoutService {

    PaymentOrderResponse createPaymentOrder(Integer purchaseId, String returnUrl, String cancelUrl);

    PaymentCaptureResponse capturePaymentOrder(String orderId);
}
