package com.example.carespawbe.entity.Expert;

import com.example.carespawbe.entity.Auth.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "appointment")
@Setter
@Getter
public class AppointmentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "app_id")
    private Long id;

    // 0: pending, 1: progress, 2: success, 3: canceled
    private Integer status;
    private BigDecimal price;
    private String userNote; // note of user to descript the problem

    @OneToOne()
    @JoinColumn(name = "slot_id")
    private AvailabilitySlotEntity slot;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity  user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_id")
    private PetEntity pet;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "expert_id")
    private ExpertEntity expert;

}
