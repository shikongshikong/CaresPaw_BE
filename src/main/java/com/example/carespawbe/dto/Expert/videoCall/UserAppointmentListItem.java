package com.example.carespawbe.dto.Expert.videoCall;

public record UserAppointmentListItem(
        Long id,
        String date,       // yyyy-MM-dd
        String startTime,  // HH:mm
        String endTime,    // HH:mm
        Integer durationMin,
        Integer status,
        String expertName,
        String petName,
        String petImageUrl
) {}
