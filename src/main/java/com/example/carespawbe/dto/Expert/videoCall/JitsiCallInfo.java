package com.example.carespawbe.dto.Expert.videoCall;

public record JitsiCallInfo(
        String roomName,
        String joinUrl,
        String jwt
) {}
