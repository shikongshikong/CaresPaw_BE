package com.example.carespawbe.entity.shop;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
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

    @Column(nullable = false)
    private String voucherName;

    @Column(nullable = false)
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
    private int voucherStatus; // 1: còn dùng được, 0: hết hạn hoặc đã khóa

    @ManyToOne
    @JoinColumn(name = "shop_id", nullable = false)
    private ShopEntity shop;

    @OneToMany(mappedBy = "voucher")
    private List<CartEntity> cartEntityList;


    @OneToMany(mappedBy = "voucherEntity")
    private List<OrderEntity> orderEntities;
}
