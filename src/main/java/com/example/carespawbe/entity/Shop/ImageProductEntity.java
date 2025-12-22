package com.example.carespawbe.entity.Shop;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;


@Entity
@Table(name = "images_product")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImageProductEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "imageProduct_id")
    private Long imageProductId;

    @Column(nullable = false)
    private String imageProductUrl;

//    @Column(nullable = false)
//    private Long source_id;
//
//    @Column(nullable = false)
//    private String source_type;

//    @Column(nullable = false)
//    private Date uploadedAt;
    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime uploadedAt;

    @UpdateTimestamp
    @Column
    private LocalDateTime updatedAt;

    @Column
    private String imagePublicId;

    @ManyToOne
//    @JoinColumn(name = "product_id", insertable = false, updatable = false)
    @JoinColumn(name = "product_id")
    private ProductEntity imageProduct;
}
