package com.example.carespawbe.entity.shop;

import com.example.carespawbe.entity.UserEntity;
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
    private Long cartId;

    @Column(updatable = false)
    private Double cartTotalPrice;

    @Column(updatable = false)
    private Double cartShippingFee;

    @Column(updatable = true)
    private int cartTotalCoinEarned;

    @Column(updatable = false)
    private LocalDate createdAt;

    @Column(updatable = true)
    private LocalDate updatedAt;

    @OneToOne
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    @ManyToOne
    @JoinColumn(name = "voucher_id")
    private VoucherEntity voucher;

    @OneToMany(mappedBy = "cart", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CartItemEntity> cartItemEntityList = new ArrayList<>();


}
