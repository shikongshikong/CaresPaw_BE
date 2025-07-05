package com.example.carespawbe.entity.shop;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @Column(nullable = false)
    private Integer productAmount;

    @Column(nullable = false)
    private Integer productStatus;

    @Column(nullable = false)
    private String productUsing;

    @Column(nullable = true)
    private String productVideoUrl;

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
}
