package com.example.carespawbe.dto.Expert.videoCall;

public record EndCallRequest(
        boolean markSuccess // true => SUCCESS, false => keep PROGRESS or set CANCELED tuỳ bạn
) {}

