package com.example.carespawbe.dto.Expert;

import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class ExpertCVUpdateRequest {
    private String biography;
    private String location;
    private BigDecimal sessionPrice;
    private String portfolioLink;

    private List<Long> categoryIds;

    private List<CertificateUpsertItem> degrees;

    @Getter @Setter
    @NoArgsConstructor @AllArgsConstructor
    @Builder
    public static class CertificateUpsertItem {
        private Long id;        // null => create
        private String title;   // map -> CertificateEntity.name
        private String issuer;  // map -> issue_place
        private Integer year;   // map -> issue_date (01/01/year)
        private String imageUrl;// map -> image
    }
}


