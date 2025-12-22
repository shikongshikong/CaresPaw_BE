package com.example.carespawbe.entity.Shop;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "category_product")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "category_id")
    private Long categoryId;

    @Column(nullable = false, columnDefinition = "NVARCHAR(255)")
    private String categoryName;

    @Column(nullable = false)
    private String categoryLogo;

    @Column(nullable = false)
    private String categoryLogoPublicId;

    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private List<ProductEntity> productCategoryList;
}
