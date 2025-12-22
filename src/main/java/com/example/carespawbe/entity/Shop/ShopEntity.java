package com.example.carespawbe.entity.Shop;

import com.example.carespawbe.entity.Auth.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
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

    @Column(nullable = false, columnDefinition = "NVARCHAR(255)")
    private String shopName;

    @Column(nullable = false, columnDefinition = "NVARCHAR(MAX)")
    private String shopAddress;

    @Column(columnDefinition = "NVARCHAR(MAX)")
    private String shopDescription;

    @Column(nullable = true)
    private String shopLogo;

    @Column(nullable = true)
    private String shopBanner;

    @Column(nullable = true)
    private int shopAmountFollower;

    @Column(nullable = false)
    private LocalDate created_at;

    @Column(nullable = false)
    private int status;

    @Column(nullable = true)
    private LocalDate update_at;

    @Column(nullable = true)
    private String shopLogoPublicId;

    @Column(nullable = true)
    private String shopBannerPublicId;

    @Column(name = "district_id")
    private Integer districtId;

    @OneToMany(mappedBy = "shop")
    private List<ProductEntity> productShopList;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true, nullable = false)
    private UserEntity user;

    @OneToMany(mappedBy = "shop")
    private List<VoucherEntity> voucherEntityList;

    @OneToMany(mappedBy = "shop")
    private List<FeedbackEntity>  feedbackList = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        this.update_at = LocalDate.now();
        this.created_at = LocalDate.now(); // nếu chưa có
    }
}
