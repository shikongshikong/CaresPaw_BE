package com.example.carespawbe.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fullName;

    private String gentle;

    @Column(nullable = false)
    private String email;

    private String phoneNumber;

    @Column(nullable = false)
    private String password;

    private String avatar, role, status;
    private LocalDate birthDate;

    private LocalDate createdAt;

    @PrePersist
    protected void onCreate() {
//        set current date
        createdAt = LocalDate.now();
//        set default avatar
        avatar = "no-avatar-img.png";
    }

}
