package com.example.carespawbe.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

//@Entity
//@Table(name = "expertEntity")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ExpertEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "expert_id")
    private Long id;

    private String introduction;
    private String experience;
    private String address;
    private String status;
    private float rating;
    private LocalDate createAt;

    @OneToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @PrePersist
    protected void onCreate() {
        status = "active";
        rating = 0.0f;
    }
}
