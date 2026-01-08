package com.example.carespawbe.dto.Expert.videoCall;

import com.example.carespawbe.enums.CallJoinState;
import lombok.AllArgsConstructor;
import lombok.Data;

//@Data
//@AllArgsConstructor
//public class JoinCallResponse {
//    private String roomId;       // e.g. "app_123"
//    private String domain;       // e.g. "meet.jit.si" or "jitsi.yourdomain.com"
//    private String joinUrl;      // e.g. "https://meet.jit.si/app_123"
//    private String displayName;  // optional
//}

//public record JoinCallResponse(
//        CallJoinState state,
//        Long appointmentId,
//        String date,        // yyyy-MM-dd
//        String startTime,   // HH:mm
//        String endTime,     // HH:mm
//        Integer durationMin,
//
//        Long serverNowEpochMs,
//        Long startAtEpochMs,
//        Long endAtEpochMs,
//
//        Integer secondsToStart,   // only when WAITING
//        Integer remainingSec,     // only when ACTIVE
//
//        JitsiCallInfo jitsi
//) {}
    public record JoinCallResponse(
            CallJoinState state,
            Long appointmentId,
            String date,            // yyyy-MM-dd
            String startTime,       // HH:mm
            String endTime,         // HH:mm
            Integer durationMin,

            Long serverNowEpochMs,
            Long startAtEpochMs,
            Long endAtEpochMs,

            Integer secondsToStart, // WAITING
            Integer remainingSec,   // ACTIVE
            JitsiCallInfo jitsi     // ACTIVE
    ) {}
