package com.example.carespawbe.entity.Expert;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "medical_record")
@Getter
@Setter
public class MedicalRecordEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 5000)
    private String expertNote;

    @Column(length = 5000)
    private String expertAdvise;

    @OneToOne
    @JoinColumn(name = "app_id")
    private AppointmentEntity appointment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_id")
    private PetEntity pet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "expert_id")
    private ExpertEntity expert;
}
