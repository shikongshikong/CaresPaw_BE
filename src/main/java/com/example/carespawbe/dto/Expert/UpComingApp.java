package com.example.carespawbe.dto.Expert;

import lombok.*;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@Builder
public class UpComingApp {
    private Long appId;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private String petImageUrl;
    private String petName;
    private String species;
    private String breed;
    private String userName;
    private Double weight;
    private String userNote;
    private Integer status;

    public UpComingApp(Long appId, LocalDate date, LocalTime startTime, LocalTime endTime, String petImageUrl, String petName, String species, String breed, String userName, Double weight, String userNote, Integer status) {
        this.appId = appId;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.petImageUrl = petImageUrl;
        this.petName = petName;
        this.species = species;
        this.breed = breed;
        this.userName = userName;
        this.weight = weight;
        this.userNote = userNote;
        this.status = status;
    }
}
