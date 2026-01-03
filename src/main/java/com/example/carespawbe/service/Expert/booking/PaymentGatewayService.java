package com.example.carespawbe.service.Expert.booking;

import com.example.carespawbe.entity.Expert.AppointmentEntity;
import com.example.carespawbe.entity.Shop.PaymentEntity;
import org.springframework.stereotype.Service;

@Service
public class PaymentGatewayService {

    public String createPaymentUrl(PaymentEntity payment, AppointmentEntity app) {
        String method = payment.getPayment_method();
        if ("vnpay".equalsIgnoreCase(method)) {
            return "https://sandbox-vnpay/pay?paymentId=" + payment.getPaymentId() + "&appId=" + app.getId();
        }
        if ("momo".equalsIgnoreCase(method)) {
            return "https://sandbox-momo/pay?paymentId=" + payment.getPaymentId() + "&appId=" + app.getId();
        }
        if ("cash".equalsIgnoreCase(method)) return null;
        throw new RuntimeException("Unsupported payment method: " + method);
    }
}

