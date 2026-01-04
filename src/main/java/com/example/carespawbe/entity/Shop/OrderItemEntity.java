package com.example.carespawbe.entity.Shop;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "order_item")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_item_id")
    private Long orderItemId;

    @Column(nullable = false)
    private Integer orderItemQuantity;

    @Column(nullable = false)
    private Double orderItemPrice;       // snapshot unit price from SKU

    @Column(nullable = false)
    private Double orderItemTotalPrice;  // price * qty

    // ✅ chuẩn 5 bảng: order item trỏ SKU
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_sku_id", nullable = false)
    private ProductSkuEntity productSku;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_order_id")
    private ShopOrderEntity shopOrder;

    @OneToMany(mappedBy = "orderItem")
    @Builder.Default
    private List<FeedbackEntity> feedbackList = new ArrayList<>();

    // ✅ snapshot để hiển thị
    @Column(name = "variant_text", columnDefinition = "NVARCHAR(255)")
    private String variantText;

    @Column(name = "sku_code", columnDefinition = "NVARCHAR(80)")
    private String skuCode;

    @Column(name = "product_name", columnDefinition = "NVARCHAR(255)")
    private String productName;
}


