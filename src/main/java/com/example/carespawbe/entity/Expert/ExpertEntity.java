package com.example.carespawbe.entity.Expert;

import com.example.carespawbe.entity.Auth.UserEntity;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "expert")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class ExpertEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "expert_id")
    private Long id;

    private String fullName;
    @Column(name = "biography", length = 2000)
    private String biography;
    private Integer experienceYear;
    private Double price;
    private Integer status; // 0:pending, 1:active, 2: block
    private String idImage;
    private String location;
    private BigDecimal sessionPrice;
    private String portfolioLink;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique = true)
    private UserEntity user;

    @OneToMany(mappedBy = "expert", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExpertToExpertCategoryEntity> expertToCategoryEntities;

    @OneToMany(mappedBy = "expert", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CertificateEntity> certificateEntities;

    @OneToMany(mappedBy = "expert", cascade = CascadeType.ALL)
    private List<AvailabilitySlotEntity> slotList;

    @OneToMany(mappedBy = "expert")
    private List<MedicalRecordEntity> medicalRecordList = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        status = 0;
    }
}
