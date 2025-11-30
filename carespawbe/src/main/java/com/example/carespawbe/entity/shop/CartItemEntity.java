package com.example.carespawbe.entity.shop;

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

    @Column(updatable = false)
    private Double cartItemOriginalPrice;

    @Column(updatable = false)
    private int cartItemQuantity;

    @Column(updatable = false)
    private Double cartItemTotalPrice;

    @Column(updatable = true)
    private boolean isFlashSale;

    @ManyToOne
    @JoinColumn(name = "cart_id")
    private CartEntity cart;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private ProductEntity product;

    @ManyToOne
    @JoinColumn(name = "product_varriant_id")
    private ProductVarriantEntity productVarriant;
}

