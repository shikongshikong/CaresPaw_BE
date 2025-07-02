package com.example.carespawbe.entity;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;


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
    private String imageProduct_url;

//    @Column(nullable = false)
//    private Long source_id;
//
//    @Column(nullable = false)
//    private String source_type;

    @Column(nullable = false)
    private Date uploaded_at;

    @ManyToOne
//    @JoinColumn(name = "product_id", insertable = false, updatable = false)
    @JoinColumn(name = "product_id")
    private ProductEntity imageProduct;
}
