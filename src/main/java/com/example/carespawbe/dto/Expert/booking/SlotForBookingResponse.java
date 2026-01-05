package com.example.carespawbe.dto.Expert.booking;

import java.math.BigDecimal;
import java.time.LocalTime;

public record SlotForBookingResponse(
        Long id,
        LocalTime startTime,
        LocalTime endTime,
        BigDecimal price,
        Integer booked
) {}

