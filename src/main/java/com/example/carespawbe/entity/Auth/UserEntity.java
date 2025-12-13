package com.example.carespawbe.entity.Auth;

import com.example.carespawbe.entity.Forum.ForumPostEntity;
import com.example.carespawbe.entity.Forum.ForumPostHistoryEntity;
import com.example.carespawbe.entity.Forum.ForumPostSaveEntity;
import com.example.carespawbe.entity.Shop.OrderEntity;
import com.example.carespawbe.entity.Shop.OrderStatusHistoryEntity;
import com.example.carespawbe.entity.Shop.ShopOrderStatusHistoryEntity;
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

    @OneToMany(mappedBy = "userEntity")
    private List<OrderEntity> orderEntities;

    private String avatar;
    private int role;
    private int state;
    private LocalDate birthday;
    private LocalDate createdAt;

//    optional, but useful when userEntity.getForumPostEntities();
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<ForumPostEntity> forumPostEntities = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<ForumPostHistoryEntity> histories = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ForumPostSaveEntity> forumPostSaveEntity;

    @OneToMany(mappedBy = "changedBy")
    private List<OrderStatusHistoryEntity> changedOrderStatuses;

    @OneToMany(mappedBy = "changedBy")
    private List<ShopOrderStatusHistoryEntity> changedShopOrderStatuses;

    @PrePersist
    protected void onCreate() {
//        set current date + avatar + role + status
        createdAt = LocalDate.now();
        avatar = "no-avatar-img.png";
        role = 1;
        state = 1;
    }

}
