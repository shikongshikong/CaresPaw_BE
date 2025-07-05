package com.example.carespawbe.entity.shop;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
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
    private String name;

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private String logo;

    @Column(nullable = false)
    private Integer amount_follower;

    @Column(nullable = false)
    private Date created_at;

    @OneToMany(mappedBy = "shop")
    private List<ProductEntity> productShopList;
}
