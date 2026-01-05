package com.example.carespawbe.dto.Expert;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalTime;

@Setter
@Getter
public class RemainApp {
    private String petImageUrl;
    private String petName;
    private String userName;
    private LocalTime startTime;
    private LocalTime endTime;
    private Integer status;

    public RemainApp(String petImageUrl, String petName, String userName, LocalTime startTime, LocalTime endTime, Integer status) {
        this.petImageUrl = petImageUrl;
        this.petName = petName;
        this.userName = userName;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
    }
}
