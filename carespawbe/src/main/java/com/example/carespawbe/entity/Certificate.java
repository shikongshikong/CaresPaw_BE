package com.example.carespawbe.entity;

import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "certificate")
public class Certificate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "certificate_id")
    private Long id;

    private String name;
    private String issue_place;
    private String issue_date;
    private String image;
    private String field;
    private String expiry_date;

//    unknown address + avatar

    @ManyToOne
    @JoinColumn(name = "expert_id")
    private Expert expert;

}
