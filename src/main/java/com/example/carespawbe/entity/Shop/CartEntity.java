package com.example.carespawbe.entity.Shop;

import com.example.carespawbe.entity.Auth.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "cart")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id")
    private Long cartId;

    @Column(updatable = false)
    private Double CartTotalPrice;

//    @Column(updatable = false)
//    private Double CartShippingFee;
//
//    @Column(updatable = true)
//    private int CartTotalCoinEarned;

    @Column(updatable = false)
    private LocalDate createdAt;

    @Column(updatable = true)
    private LocalDate updatedAt;

    @OneToOne
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<CartItemEntity> cartItemEntityList = new ArrayList<>();


}
