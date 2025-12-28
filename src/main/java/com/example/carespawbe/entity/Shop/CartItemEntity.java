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

    @Column(updatable = true)
    private int cartItemQuantity;

    @Column(updatable = true)
    private Double cartItemTotalPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id")
    private CartEntity cart;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private ProductEntity product;

    @Column(name = "selected_value_ids", columnDefinition = "NVARCHAR(MAX)")
    private String selectedValueIds; // ví dụ: "[11,22]"

    @Column(name = "variant_text", columnDefinition = "NVARCHAR(255)")
    private String variantText; // ví dụ: "Color: Red, Size: M"


//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "product_varriant_id")
//    private ProductVarriantEntity productVarriant;

}

