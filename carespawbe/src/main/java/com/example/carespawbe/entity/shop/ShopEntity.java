package com.example.carespawbe.entity.shop;

import com.example.carespawbe.entity.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "shop")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShopEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shop_id")
    private Long shopId;

    @Column(nullable = false)
    private String shopName;

    @Column(nullable = false)
    private String shopAddress;

    @Column(nullable = true)
    private String shopLogo;

    @Column(nullable = false)
    private int shopAmountFollower;

    @Column(nullable = false)
    private LocalDateTime created_at;

    @Column(nullable = false)
    private int status;

    @Column(nullable = true)
    private LocalDateTime update_at;

    @Column(nullable = true)
    private String shopLogoPublicId;

    @OneToMany(mappedBy = "shop")
    private List<ProductEntity> productShopList;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true, nullable = false)
    private UserEntity user;

}
