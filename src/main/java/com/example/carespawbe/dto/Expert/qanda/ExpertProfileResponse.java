package com.example.carespawbe.dto.Expert.qanda;

import lombok.*;

import java.util.List;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor @Builder
public class ExpertProfileResponse {
    private Long id;
    private String fullName;
    private String biography;
    private Integer experienceYear;
    private String idImage;
    private Double price;
    private String portfolioLink;

    private List<CertificateItem> certificates;

    @Getter @Setter
    @NoArgsConstructor @AllArgsConstructor @Builder
    public static class CertificateItem {
        private Long id;
        private String title;
        private String issuer;
        private Object issueDate; // dùng Object để bạn map LocalDate/LocalDateTime tuỳ entity bạn
    }
}
