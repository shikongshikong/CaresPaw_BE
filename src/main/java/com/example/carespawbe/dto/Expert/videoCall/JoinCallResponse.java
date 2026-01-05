package com.example.carespawbe.dto.Expert.videoCall;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class JoinCallResponse {
    private String roomId;       // e.g. "app_123"
    private String domain;       // e.g. "meet.jit.si" or "jitsi.yourdomain.com"
    private String joinUrl;      // e.g. "https://meet.jit.si/app_123"
    private String displayName;  // optional
}

