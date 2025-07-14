package com.example.carespawbe.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    @Column(nullable = false)
    private String fullname;

    private String gender;

    @Column(nullable = false)
    private String email;

    private String phoneNumber;

    @Column(name = "pass_word", nullable = false)
    private String password;

    private String avatar;
    private String role;
    private String status;
    private LocalDate birthday;
    private LocalDate createdAt;

//    optional, but useful when user.getPosts();
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
//    @JsonManagedReference
    private List<ForumPost> posts = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
//        set current date + avatar + role + status
        createdAt = LocalDate.now();
        avatar = "no-avatar-img.png";
        role = "normal";
        status = "active";
    }

}
