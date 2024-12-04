package com.hoteleria.hoteleria.Service.impl;

import com.hoteleria.hoteleria.DTO.PaymentCaptureResponse;
import com.hoteleria.hoteleria.DTO.PaymentOrderResponse;
import com.hoteleria.hoteleria.DTO.PurchaseDTO;
import com.hoteleria.hoteleria.integration.payment.paypal.dto.OrderCaptureResponse;
import com.hoteleria.hoteleria.integration.payment.paypal.dto.OrderResponse;
import com.hoteleria.hoteleria.integration.payment.paypal.service.PaypalService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CheckoutServiceImpl implements CheckoutService{
    private final PaypalService payPalService;
    private final PurchaseService purchaseService;

    @Override
    public PaymentOrderResponse createPaymentOrder(Integer purchaseId, String returnUrl, String cancelUrl) {
        OrderResponse orderResponse = payPalService.createOrder(purchaseId, returnUrl, cancelUrl);
        String paypalUrl = orderResponse
                .getLinks()
                .stream()
                .filter(link -> link.getRel().equals("approve"))
                .findFirst()
                .orElseThrow(RuntimeException::new)
                .getHref();
        return new PaymentOrderResponse(paypalUrl);
    }

    @Override
    public PaymentCaptureResponse capturePaymentOrder(String orderId) {
        OrderCaptureResponse orderCaptureResponse = payPalService.captureOrder(orderId);
        boolean completed = orderCaptureResponse.getStatus().equals("COMPLETED");

        PaymentCaptureResponse paypalCaptureResponse = new PaymentCaptureResponse();
        paypalCaptureResponse.setCompleted(completed);

        if (completed) {
            String purchaseIdStr = orderCaptureResponse.getPurchaseUnits().get(0).getReferenceId();
            PurchaseDTO purchaseDTO = purchaseService.confirmPurchase(Integer.parseInt(purchaseIdStr));
            paypalCaptureResponse.setPurchaseId(purchaseDTO.getId());
        }
        return paypalCaptureResponse;
    }

}