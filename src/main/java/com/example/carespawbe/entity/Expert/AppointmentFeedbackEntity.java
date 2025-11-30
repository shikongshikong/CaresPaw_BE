//package com.example.carespawbe.entity;
//
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import java.time.LocalDate;
//
////@Entity
////@Table(name = "appointment_feedback")
//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//@Builder
//public class AppointmentFeedbackEntity {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "app_feedback_id")
//    private Long id;
//
//    private int rating;
//    private String content;
//    private LocalDate createAt;
//
//    @ManyToOne
//    @JoinColumn(name = "user_id")
//    private UserEntity user;
//
//    @ManyToOne
//    @JoinColumn(name = "expert_id")
//    private ExpertEntity expertEntity;
//
//
//}
