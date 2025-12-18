package com.example.carespawbe.entity.Shop;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cart_item")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long cartItemId;

    @Column(updatable = true)
    private Double cartItemPrice;

//    @Column(updatable = true)
//    private Double cartItemOriginalPrice;

    @Column(updatable = true)
    private int cartItemQuantity;

    @Column(updatable = true)
    private Double cartItemTotalPrice;

//    @Column(updatable = true)
//    private boolean isFlashSale;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private CartEntity cart;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private ProductEntity product;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "product_varriant_id")
//    private ProductVarriantEntity productVarriant;

}

