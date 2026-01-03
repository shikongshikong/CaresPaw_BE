package com.example.carespawbe.dto.Expert;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Setter
@Getter
public class SlotSettingRequest {
    private LocalDate date;
    private LocalTime startTime;
    private Integer duration;
}
