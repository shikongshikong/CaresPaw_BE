package com.example.carespawbe.dto.Expert.qanda;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class PetResponse {
    private Long id;
    private String name;
    private String breed;
    private Integer gender;
    private LocalDate dateOfBirth;
    private Double weight;
    private String imageUrl;

    // species (Category)
    private Integer speciesId;
    private String speciesName;
}

