package com.example.carespawbe.dto.Expert;

import lombok.*;
import java.math.BigDecimal;
import java.util.List;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
public class ExpertCVResponse {
    private String fullName;
    private String biography;
    private Integer yearsOfExperience;

    private String cccdImageUrl; // idImage
    private String address;      // location
    private BigDecimal sessionPrice;

    // optional: nếu user có
    private String phone;
    private String avatarUrl;
    private String portfolioLink;

    private List<Long> categoryIds;

    private List<CertificateItem> degrees;

    @Getter @Setter
    @NoArgsConstructor @AllArgsConstructor
    @Builder
    public static class CertificateItem {
        private Long id;
        private String title;     // certificate.name
        private String issuer;    // issue_place
        private Integer year;     // issue_date.year
        private String imageUrl;  // image
        private Integer status;   // 0/1/2
    }
}

