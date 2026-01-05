package com.example.carespawbe.dto.Expert.videoCall;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
@AllArgsConstructor
public class VetAppDetailResponse {
    private Long id;
    private Integer status;

    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;

    private BigDecimal price;
    private String userNote;

    private Long expertId;
    private String expertName;
    private String expertAvatar;
    private String expertEmail; // optional

    private Long petId;
    private String petName;
    private String petBreed;
}

