package com.example.carespawbe.entity.Shop;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "voucher")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VoucherEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long voucherId;

    @Column(nullable = false, columnDefinition = "NVARCHAR(100)")
    private String voucherName;

    @Column(nullable = false, columnDefinition = "NVARCHAR(255)")
    private String voucherDescribe;

    @Column(nullable = false)
    private Double voucherValue;

    // "%" or "VND"
    @Column(nullable = false)
    private String voucherType;

    @Column(nullable = false)
    private LocalDate startedAt;

    @Column(nullable = false)
    private LocalDate finishedAt;

    @Column(nullable = false)
    private int voucherAmount; // Số lượng còn lại

    @Column(nullable = false)
    private int voucherStatus; // 1: còn dùng được, 0: không hoạt động, 2: sắp tới, 3: hết hạn

    @Column(nullable = false)
    private int voucherUsageType; //1: voucher product, 2: voucher order

    @Column(nullable = false)
    private int voucherMinOrder;

    // FK: voucher.shop_id -> shop.shop_id
    @ManyToOne
    @JoinColumn(name = "shop_id", nullable = false)
    private ShopEntity shop;

    @OneToMany(mappedBy = "orderVoucher")
    private List<ShopOrderEntity> asOrderVoucherInShopOrders = new ArrayList<>();

    @OneToMany(mappedBy = "shippingVoucher")
    private List<ShopOrderEntity> asShippingVoucherInShopOrders = new ArrayList<>();

//    @OneToMany(mappedBy = "voucherEntity")
//    private List<OrderEntity> orderEntities;
}
