package com.example.carespawbe.dto.Expert.booking;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class BookingCheckoutRequest {
    private Long slotId;
    private PetSelection pet;
    private String userNote;
    private PaymentInfo payment;
}
