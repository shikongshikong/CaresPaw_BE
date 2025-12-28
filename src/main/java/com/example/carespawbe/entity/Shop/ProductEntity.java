package com.example.carespawbe.entity.Shop;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "product")
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Long productId;

    @Column(nullable = false, columnDefinition = "NVARCHAR(255)")
    private String productName;

    @Column(nullable = false)
    private Double productPrice;

    @Column(nullable = false)
    private Integer productAmount;

    @Column(nullable = false)
    private Integer productStatus;

    @Column(nullable = false, columnDefinition = "NVARCHAR(MAX)")
    private String productUsing;

    @Column(nullable = true)
    private String productVideoUrl;

    @Column
    private String productVideoPublicId;

    @Column(nullable = false)
    private LocalDate productCreatedAt;

    @Column(nullable = true)
    private LocalDate productUpdatedAt;

    @Column(name = "sold", columnDefinition = "bigint default 0")
    private Long sold = 0L; // Số lượng đã bán

    @Column(name = "rating", columnDefinition = "double default 0.0")
    private Double rating = 0.0; // Điểm trung bình sao

    @ManyToOne
    @JoinColumn(name = "shop_id", nullable = false)
    private ShopEntity shop;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private CategoryEntity category;

    @OneToMany(mappedBy = "productVarriants",fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ProductVarriantEntity> productVarriantList = new ArrayList<>();

    @OneToMany(mappedBy = "imageProduct", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<ImageProductEntity> imageProductList = new ArrayList<>();

}
