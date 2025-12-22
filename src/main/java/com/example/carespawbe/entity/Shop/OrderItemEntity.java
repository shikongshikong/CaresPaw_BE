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
    private int orderItemQuantity;

    @Column(nullable = false)
    private Double orderItemPrice;

    @Column(nullable = false)
    private Double orderItemTotalPrice;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private ProductEntity product;

    // FK: order_item.shop_order_id -> shop_orders.shop_order_id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_order_id")
    private ShopOrderEntity shopOrder;

    @OneToMany(mappedBy = "orderItem")
    private List<FeedbackEntity> feedbackList = new ArrayList<>();

}

