package com.example.carespawbe.dto.Expert.booking;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PetSelection {
    private String mode; // "select" | "create"
    private Long selectedPetId; // nếu select
    private PetCreateInfo create; // nếu create
}

