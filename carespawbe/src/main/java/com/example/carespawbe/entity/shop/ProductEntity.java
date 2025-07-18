package com.example.carespawbe.entity.shop;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "product")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long productId;

    @Column(nullable = false)
    private String productName;

    @Column(nullable = false)
    private String productDescribe;

    @Column(nullable = false)
    private Double productPrice;

    @Column(nullable = true)
    private Double productPriceSale;

    @Column(nullable = false)
    private Integer productAmount;

    @Column(nullable = false)
    private Integer productStatus;

    @Column(nullable = false)
    private String productUsing;

    @Column(nullable = true)
    private String productVideoUrl;

    @Column
    private String productVideoPublicId;

    @Column(nullable = false)
    private LocalDate productCreatedAt;

    @Column(nullable = true)
    private LocalDate productUpdatedAt;

    @ManyToOne
    @JoinColumn(name = "shop_id", nullable = false)
    private ShopEntity shop;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private CategoryEntity category;

    @OneToMany(mappedBy = "productVarriants")
    private List<ProductVarriantEntity> productVarriantList;

    @OneToMany(mappedBy = "imageProduct")
    private List<ImageProductEntity> imageProductList;

    @OneToMany(mappedBy = "product")
    private List<CartItemEntity> cartItemList;

//    @PrePersist
//    protected void onCreate() {
//        productCreatedAt = LocalDate.now();
//        productUpdatedAt = LocalDate.now();
//    }

}
