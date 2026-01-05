package com.example.carespawbe.dto.Expert.booking;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BookingCheckoutResult {
    private Long appointmentId;
    private Long paymentId;
    private String paymentUrl; // null nếu cash
}
