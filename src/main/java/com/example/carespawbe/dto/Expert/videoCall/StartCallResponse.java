package com.example.carespawbe.dto.Expert.videoCall;

public record StartCallResponse(
        Long appointmentId,
        Integer status, // should be PROGRESS
        String roomName,
        String joinUrl,
        String jwt // optional
) {}

