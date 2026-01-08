package com.example.carespawbe.dto.Expert.videoCall;

public record UserAppointmentDetailResponse(
        Long id,
        Integer status,
        Integer durationMin,
        String userNote,

        String date,
        String startTime,
        String endTime,

        String expertName,
        String expertEmail,

        String petName,
        String petBreed,
        String petImageUrl
) {}
