package com.example.carespawbe.dto.Shop.request;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemRequest {
    // FE của bạn đang có cartItemId => BE sẽ lấy product từ cartItem
    private Long cartItemId;

    // order_item.order_item_quantity
    private Integer orderItemQuantity;

    // order_item.order_item_price (BE có thể lấy từ cartItem/cart)
    private Double orderItemPrice;

    // order_item.order_item_total_price
    private Double orderItemTotalPrice;

    // FK -> product_varriant.product_varriant_id
    // (nếu FE không gửi thì BE tự chọn default variant của product)
//    private Long productVarriantId;
    private Long productId;
}
