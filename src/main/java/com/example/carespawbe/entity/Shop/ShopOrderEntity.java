package com.example.carespawbe.entity.Shop;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(
        name = "shop_orders",
        indexes = {
                @Index(name = "IX_shop_orders_order_id", columnList = "order_id"),
                @Index(name = "IX_shop_orders_shop_id", columnList = "shop_id")
        }
)
public class ShopOrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "shop_order_id")
    private Long shopOrderId;

    @Column(name = "shipping_fee", nullable = false)
    private Double shippingFee;

    @Column(name = "shop_order_status", nullable = false)
    private int shopOrderStatus;

//    @Column(name = "ghn_service_id")
//    private int ghnServiceId;

    @Column(name = "ghn_order_code")
    private String ghnOrderCode;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private OrderEntity order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_id", nullable = false)
    private ShopEntity shop;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_voucher_id")
    private VoucherEntity orderVoucher;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shipping_voucher_id")
    private VoucherEntity shippingVoucher;

    // 1 shop_order -> N order_item
    @OneToMany(mappedBy = "shopOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderItemEntity> orderItemEntities = new ArrayList<>();

    // 1 shop_order -> N history
    @OneToMany(mappedBy = "shopOrder", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ShopOrderStatusHistoryEntity> shopOrderStatusHistories = new ArrayList<>();

}
