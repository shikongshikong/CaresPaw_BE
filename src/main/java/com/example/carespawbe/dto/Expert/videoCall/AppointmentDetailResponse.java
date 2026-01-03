package com.example.carespawbe.dto.Expert.videoCall;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

public record AppointmentDetailResponse(
        Long appointmentId,
        Integer status,
        BigDecimal price,
        String userNote,

        Long slotId,
        LocalDate date,
        LocalTime startTime,
        LocalTime endTime,

        PetSnapshot pet,
        UserSnapshot user,
        ExpertSnapshot expert,

        JitsiCallInfo jitsi
) {
    public record PetSnapshot(
            Long id,
            String name,
            String breed,
            Integer gender,
            String dateOfBirth, // string để nhẹ DTO (hoặc LocalDate)
            String imageUrl,
            String description,
            String microchipId,
            String allergies,
            String chronicDiseases,
            Double weight
    ) {}

    public record UserSnapshot(Long id, String fullName) {}
    public record ExpertSnapshot(Long id, String fullName) {}

    public record JitsiCallInfo(
            String roomName,
            String joinUrl,
            String jwt // optional: có thể null
    ) {}
}

