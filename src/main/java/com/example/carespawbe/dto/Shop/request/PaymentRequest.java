package com.example.carespawbe.dto.Shop.request;

import com.example.carespawbe.entity.Shop.OrderEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentRequest {
    private String paymentMethod;
    private Double pricePayment;
    private Long paymentCode;
    private String description;

}
