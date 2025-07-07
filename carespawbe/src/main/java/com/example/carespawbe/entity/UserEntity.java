package com.example.carespawbe.entity;

import com.example.carespawbe.entity.shop.CartEntity;
import com.example.carespawbe.entity.shop.ShopEntity;
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
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fullname;

    private int gender;

    @Column(nullable = false)
    private String email;

    private String phoneNumber;

    @Column(name = "pass_word", nullable = false)
    private String password;

    private String avatar;
    private int role;
    private int status;
    private LocalDate birthday;
    private LocalDate createdAt;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private ShopEntity shop;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private CartEntity cart;


    @PrePersist
    protected void onCreate() {
//        set current date + avatar + role + status
        createdAt = LocalDate.now();
        avatar = "no-avatar-img.png";
        role = 1;
        status = 1;
    }

}
