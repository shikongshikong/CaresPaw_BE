package com.example.carespawbe.dto.Expert.booking;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class PetCreateInfo {
    private String name;
    private String breed;
    private Integer gender;
    private LocalDate dateOfBirth;
    private String description;
    private String microchipId;
    private String allergies;
    private String chronicDiseases;
    private Double weight;
    private Long speciesId;
}


