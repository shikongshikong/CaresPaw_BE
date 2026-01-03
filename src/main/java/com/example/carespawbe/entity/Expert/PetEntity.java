package com.example.carespawbe.entity.Expert;

import com.example.carespawbe.entity.Auth.UserEntity;
import com.example.carespawbe.entity.Shop.CategoryEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "pet")
@Setter
@Getter
public class PetEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "pet_id")
    private Long id;

    private String name;
    private String breed;
    private Integer gender;
    private LocalDate dateOfBirth;
    private String imageUrl;
    private String description;
    private String microchipId;
    private String allergies;
    private String chronic_diseases;
    private Double weight;

    @ManyToOne(fetch = FetchType.LAZY)
    private CategoryEntity species;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @OneToMany(mappedBy = "pet")
    private List<AppointmentEntity> appointmentList = new ArrayList<>();

    @OneToMany(mappedBy = "pet")
    private List<MedicalRecordEntity> medicalRecordList = new ArrayList<>();

}
