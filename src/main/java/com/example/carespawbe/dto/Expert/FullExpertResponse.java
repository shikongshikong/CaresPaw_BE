package com.example.carespawbe.dto.Expert;

import lombok.AllArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
public class FullExpertResponse {
    private Long id;
    private String name;
    private String description;
    private String experience;
    private float rating;
    private String email;
    private String phoneNum;
    private String image;
    private LocalDate createAt;
    private int appointmentsAmount;
    private int reportsAmount;
    private String status;
}
