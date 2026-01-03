package com.example.carespawbe.dto.Expert;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CalendarItemResponse {
    private String id;       // "slot_{id}" hoặc "app_{id}"
    private String dateKey;  // yyyy-MM-dd
    private int startMin;    // minutes from 00:00
    private int endMin;
    private String patient;  // fullname hoặc "—"
    private String status;   // pending/scheduled/in_progress/completed/cancelled
}

