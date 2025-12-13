package com.example.carespawbe.entity.Shop;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

//    @Column(updatable = false)
//    private boolean flashSale;

//    @ManyToOne
//    @JoinColumn(name = "order_id")
//    private OrderEntity orderEntity;

    // FK: order_item.product_varriant_id -> product_varriant.product_varriant_id
    @ManyToOne
    @JoinColumn(name = "product_varriant_id")
    private ProductVarriantEntity productVarriantEntity;

    // FK: order_item.shop_order_id -> shop_orders.shop_order_id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shop_order_id")
    private ShopOrderEntity shopOrderId;

}

