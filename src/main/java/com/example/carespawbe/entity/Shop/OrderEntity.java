package com.example.carespawbe.entity.Shop;
import com.example.carespawbe.entity.Auth.UserAddressEntity;
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
@Table(name = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private Long orderId;

    @Column(nullable = false)
    private Double orderShippingFee;

    @Column(nullable = false)
    private Double orderTotalPrice;

    @Column(nullable = false)
    private LocalDate orderCreatedAt;

    @Column(nullable = true)
    private LocalDate orderUpdatedAt;

    @Column(nullable = false)
    private Integer orderStatus;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    // 1 order -> N shop_orders
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ShopOrderEntity> shopOrders = new ArrayList<>();

    // 1 order -> N history
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderStatusHistoryEntity> orderStatusHistories = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "payment_id")
    private PaymentEntity paymentEntity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "address_id")
    private UserAddressEntity shippingAddress;

}
