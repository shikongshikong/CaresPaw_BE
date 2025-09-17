package com.example.carespawbe.entity.shop;
import com.example.carespawbe.entity.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
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
    private int orderCoinUsed;

    @Column(nullable = false)
    private Double orderTotalPrice;

    @Column(nullable = false)
    private LocalDate orderCreatedAt;

    @Column(nullable = true)
    private LocalDate orderUpdatedAt;

    @Column(nullable = false)
    private int orderStatus;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;

    @ManyToOne
    @JoinColumn(name = "voucher_id")
    private VoucherEntity voucherEntity;

    @OneToMany(mappedBy = "orderEntity")
    private List<OrderItemEntity> orderItems;

    @ManyToOne
    @JoinColumn(name = "payment_id")
    private PaymentEntity paymentEntity;
}
