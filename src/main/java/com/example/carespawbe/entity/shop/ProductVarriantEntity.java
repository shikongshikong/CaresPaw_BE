package com.example.carespawbe.entity.shop;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "product_varriant")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductVarriantEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_varriant_id")
    private Long productVarriantId;

    @Column(nullable = false)
    private String productVarriantValue;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private ProductEntity productVarriants;

    @ManyToOne
    @JoinColumn(name = "varriant_id")
    private VarriantEntity varriants;

//    @OneToMany(mappedBy = "productVarriant")
//    private List<CartItemEntity> cartItemEntityList;
//    @OneToMany(mappedBy = "productVarriant")
//    private List<CartItemEntity> cartItemEntityList;

    @OneToMany(mappedBy = "productVarriantEntity")
    private List<OrderItemEntity> orderItemEntityList;
}
