package com.example.carespawbe.entity.Auth;

import com.example.carespawbe.entity.shop.OrderEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
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
    private int state;
    private LocalDate birthday;
    private LocalDate createdAt;

    @OneToMany(mappedBy = "userEntity")
    private List<OrderEntity> orderEntities;

//    optional, but useful when user.getPosts();
//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
//    private List<Post> posts = new ArrayList<>();
//
//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
//    private List<PostHistory> histories = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
//        set current date + avatar + role + status
        createdAt = LocalDate.now();
        avatar = "no-avatar-img.png";
        role = 1;
        state = 1;
    }

}
