package com.example.carespawbe.serviceImp.Shop;

import com.example.carespawbe.dto.Shop.request.OrderItemRequest;
import com.example.carespawbe.dto.Shop.request.OrderRequest;
import com.example.carespawbe.dto.Shop.request.ShopOrderRequest;
import com.example.carespawbe.dto.Shop.response.OrderResponse;
import com.example.carespawbe.entity.Auth.UserAddressEntity;
import com.example.carespawbe.entity.Auth.UserEntity;
import com.example.carespawbe.entity.Shop.*;
import com.example.carespawbe.mapper.Shop.OrderMapper;
import com.example.carespawbe.repository.Auth.UserAddressRepository;
import com.example.carespawbe.repository.Shop.*;
import com.example.carespawbe.service.Shop.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImp implements OrderService {

    @Autowired private CartItemRepository cartItemRepo;
    @Autowired private OrderRepository orderRepo;
    @Autowired private ShopOrderRepository shopOrderRepo;
    @Autowired private VoucherRepository voucherRepo;
    @Autowired private ShopRepository shopRepo;
    @Autowired private ProductRepository productRepo;          // ✅ dùng ProductRepository
    @Autowired private UserAddressRepository userAddressRepo;
    @Autowired private OrderMapper orderMapper;

    @Override
    @Transactional
    public OrderResponse checkout(Long userId, OrderRequest req) {

        if (userId == null) throw new RuntimeException("userId is required");
        if (req == null || req.getShopOrders() == null || req.getShopOrders().isEmpty()) {
            throw new RuntimeException("shopOrders is empty");
        }

        // ==== 1) gom tất cả cartItemId từ request ====
        List<Long> cartItemIds = req.getShopOrders().stream()
                .flatMap(so -> (so.getOrderItems() == null ? List.<OrderItemRequest>of() : so.getOrderItems()).stream())
                .map(OrderItemRequest::getCartItemId)
                .filter(Objects::nonNull)
                .distinct()
                .toList();

        if (cartItemIds.isEmpty()) {
            throw new RuntimeException("No items to checkout");
        }

        // ==== 2) lấy cart items thuộc user ====
        List<CartItemEntity> cartItems = cartItemRepo.findCheckoutItems(userId, cartItemIds);

        if (cartItems.size() != cartItemIds.size()) {
            throw new RuntimeException("Some cart items not found or not belong to user");
        }

        Map<Long, CartItemEntity> cartById = cartItems.stream()
                .collect(Collectors.toMap(CartItemEntity::getCartItemId, Function.identity()));

        // ==== 3) validate address ====
        UserAddressEntity address = userAddressRepo
                .findByAddressIdAndUser_IdAndIsDeletedFalse(req.getShippingAddressId(), userId)
                .orElseThrow(() -> new RuntimeException("Shipping address not found"));

        // ==== 4) tạo OrderEntity ====
        OrderEntity order = OrderEntity.builder()
                .orderShippingFee(req.getOrderShippingFee())
                .orderTotalPrice(req.getOrderTotalPrice())
                .orderCreatedAt(LocalDate.now())
                .orderStatus(req.getOrderStatus() == null ? 1 : req.getOrderStatus())
                .userEntity(UserEntity.builder().id(userId).build())
                .shippingAddress(address)
                .build();

        order = orderRepo.save(order);

        // ==== 5) tạo ShopOrder + OrderItem theo từng shopOrders request ====
        List<ShopOrderEntity> shopOrdersToSave = new ArrayList<>();

        for (ShopOrderRequest soReq : req.getShopOrders()) {

            ShopEntity shop = shopRepo.findById(soReq.getShopId())
                    .orElseThrow(() -> new RuntimeException("Shop not found: " + soReq.getShopId()));

            VoucherEntity orderVoucher = null;
            if (soReq.getOrderVoucherId() != null) {
                orderVoucher = voucherRepo.findById(soReq.getOrderVoucherId())
                        .orElseThrow(() -> new RuntimeException("Order voucher not found: " + soReq.getOrderVoucherId()));
            }

            VoucherEntity shippingVoucher = null;
            if (soReq.getShippingVoucherId() != null) {
                shippingVoucher = voucherRepo.findById(soReq.getShippingVoucherId())
                        .orElseThrow(() -> new RuntimeException("Shipping voucher not found: " + soReq.getShippingVoucherId()));
            }

            ShopOrderEntity shopOrder = new ShopOrderEntity();
            shopOrder.setOrder(order);
            shopOrder.setShop(shop);
            shopOrder.setShippingFee(soReq.getShippingFee() == null ? 0.0 : soReq.getShippingFee());
            shopOrder.setShopOrderStatus(soReq.getShopOrderStatus() == null ? 1 : soReq.getShopOrderStatus());
//            shopOrder.setGhnServiceId(soReq.getGhnServiceId() == null ? 0 : soReq.getGhnServiceId());
            shopOrder.setCreatedAt(LocalDateTime.now());
            shopOrder.setUpdatedAt(null);
            shopOrder.setOrderVoucher(orderVoucher);
            shopOrder.setShippingVoucher(shippingVoucher);

            // items của shopOrder
            List<OrderItemEntity> orderItems = new ArrayList<>();

            if (soReq.getOrderItems() != null) {
                for (OrderItemRequest itemReq : soReq.getOrderItems()) {

                    CartItemEntity ci = cartById.get(itemReq.getCartItemId());
                    if (ci == null) throw new RuntimeException("CartItem not found: " + itemReq.getCartItemId());

                    int qty = (itemReq.getOrderItemQuantity() == null || itemReq.getOrderItemQuantity() < 1)
                            ? 1
                            : itemReq.getOrderItemQuantity();

                    Double price = itemReq.getOrderItemPrice();
                    if (price == null) price = ci.getCartItemPrice();

                    Double total = itemReq.getOrderItemTotalPrice();
                    if (total == null) total = price * qty;

                    // ✅ NEW: OrderItemEntity lưu product_id
                    // Ưu tiên lấy product từ cartItem để đảm bảo đúng dữ liệu thuộc user
                    ProductEntity product = ci.getProduct();
                    if (product == null) throw new RuntimeException("CartItem has no product: " + ci.getCartItemId());

                    // Nếu bạn muốn bắt buộc FE gửi productId và verify khớp:
                    if (itemReq.getProductId() != null && !Objects.equals(product.getProductId(), itemReq.getProductId())) {
                        throw new RuntimeException("Product mismatch for cartItemId=" + ci.getCartItemId());
                    }

                    OrderItemEntity oi = OrderItemEntity.builder()
                            .orderItemQuantity(qty)
                            .orderItemPrice(price)
                            .orderItemTotalPrice(total)
                            .product(product)          // ✅ set product
                            .shopOrder(shopOrder)      // ✅ set shopOrder
                            .build();

                    orderItems.add(oi);
                }
            }

            shopOrder.setOrderItemEntities(orderItems); // cascade ALL => lưu luôn order_item
            shopOrdersToSave.add(shopOrder);
        }

        shopOrderRepo.saveAll(shopOrdersToSave);

        // ==== 6) xoá cart items sau checkout ====
        cartItemRepo.deleteAllByCart_UserEntity_IdAndCartItemIdIn(userId, cartItemIds);

        // ==== 7) trả response ====
        return orderMapper.toResponse(order);
    }

    @Override
    public List<OrderResponse> getOrderByUserId(Long userId) {
        List<OrderEntity>  order = orderRepo.findAllByUserEntity_Id(userId);
        return orderMapper.toResponseList(order);
    }
}
