package com.example.carespawbe.dto.Expert;

import lombok.*;
import java.time.LocalDate;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class CertificateResponse {
    private Long id;
    private String name;
    private String credentialID;
    private String issue_place;
    private LocalDate issue_date;
    private LocalDate expiry_date;
    private String image;
    private Integer status;
//    private String adminNote;
}

