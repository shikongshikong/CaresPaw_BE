package com.example.carespawbe.dto.Expert.videoCall;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

public record AppointmentListItemResponse(
        Long appointmentId,
        Integer status,
        BigDecimal price,
        String userNote,

        Long slotId,
        LocalDate date,
        LocalTime startTime,
        LocalTime endTime,

        Long petId,
        String petName,
        String petBreed,
        Integer petGender,
        String petImageUrl,

        Long userId,
        String userFullName,

        Long expertId,
        String expertFullName
) {}

