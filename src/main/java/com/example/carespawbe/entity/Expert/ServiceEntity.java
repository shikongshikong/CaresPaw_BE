//package com.example.carespawbe.entity;
//
//import jakarta.persistence.*;
//import lombok.*;
//
//import java.time.LocalDate;
//
////@Entity
////@Table(name = "service")
//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//@Builder
//public class ServiceEntity {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "service_id")
//    private Long id;
//
//    private String name;
//    private String category;
//    private String image;
//    private String description;
//    private String address;
//    private String price;
//    private String status;
//    private LocalDate creatAt;
//    private String duration;
//    private float rating;
//
//    @ManyToOne
//    @JoinColumn(name = "user_id")
//    private UserEntity user;
//
//    @PrePersist
//    protected void onCreate() {
//        status = "active";
//        creatAt = LocalDate.now();
//        rating = 0.0f;
//    }
//}
