package com.example.carespawbe.dto.Expert.qanda;
import lombok.*;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class ExpertCardResponse {
    private Long id;
    private String fullName;
    private String idImage;
    private String location;
    private Integer experienceYear;
    private Integer status;

    // ONLY ONE price field
    private Double price;

    private String portfolioLink;
}




