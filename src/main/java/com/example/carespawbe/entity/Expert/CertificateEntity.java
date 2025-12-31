package com.example.carespawbe.entity.Expert;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Table(name = "certificate")
@Setter
@Getter
public class CertificateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "certificate_id")
    private Long id;

    private String name;
    private String credentialID;
    private String issue_place;
    private LocalDate issue_date;
    private LocalDate expiry_date;
    private String image;
    private Integer status;

    @ManyToOne
    @JoinColumn(name = "expert_id")
    private ExpertEntity expert;

//    @PrePersist
//    protected void onCreate() {
//        status = 0;
//    }

    public CertificateEntity() {

    }

    public CertificateEntity(ExpertEntity expertEntity, String image, Integer status) {
        this.expert = expertEntity;
        this.image = image;
        this.status = status;
    }

}
