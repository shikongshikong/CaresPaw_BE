package com.example.carespawbe.serviceImp.Shop;

import com.example.carespawbe.dto.Shop.request.OrderItemRequest;
import com.example.carespawbe.dto.Shop.request.OrderRequest;
import com.example.carespawbe.dto.Shop.request.PaymentRequest;
import com.example.carespawbe.dto.Shop.request.ShopOrderRequest;
import com.example.carespawbe.dto.Shop.response.OrderResponse;
import com.example.carespawbe.dto.Shop.response.ShopOrderResponse;
import com.example.carespawbe.entity.Auth.UserAddressEntity;
import com.example.carespawbe.entity.Auth.UserEntity;
import com.example.carespawbe.entity.Shop.*;
import com.example.carespawbe.enums.OrderStatus;
import com.example.carespawbe.mapper.Shop.OrderItemMapper;
import com.example.carespawbe.mapper.Shop.OrderMapper;
import com.example.carespawbe.mapper.Shop.ShopOrderMapper;
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
    @Autowired private ProductRepository productRepo;
    @Autowired private UserAddressRepository userAddressRepo;

    // ✅ [NEW] Thêm Repository này để tính tổng Sold
    @Autowired private OrderItemRepository orderItemRepo;

    @Autowired private OrderMapper orderMapper;
    @Autowired
    private ShopOrderMapper shopOrderMapper;

    @Override
    @Transactional
    public OrderResponse checkout(Long userId, OrderRequest req) {
        // ... (Giữ nguyên logic checkout cũ của bạn) ...
        if (userId == null) throw new RuntimeException("userId is required");
        if (req == null || req.getShopOrders() == null || req.getShopOrders().isEmpty()) {
            throw new RuntimeException("shopOrders is empty");
        }

        List<Long> cartItemIds = req.getShopOrders().stream()
                .flatMap(so -> (so.getOrderItems() == null ? List.<OrderItemRequest>of() : so.getOrderItems()).stream())
                .map(OrderItemRequest::getCartItemId)
                .filter(Objects::nonNull)
                .distinct()
                .toList();

        if (cartItemIds.isEmpty()) {
            throw new RuntimeException("No items to checkout");
        }

        List<CartItemEntity> cartItems = cartItemRepo.findCheckoutItems(userId, cartItemIds);

        Set<Long> foundIds = cartItems.stream()
                .map(CartItemEntity::getCartItemId)
                .collect(Collectors.toSet());

        List<Long> missingIds = cartItemIds.stream()
                .filter(id -> !foundIds.contains(id))
                .toList();

        if (!missingIds.isEmpty()) {
            throw new RuntimeException("Cart items not found/not belong to user: " + missingIds);
        }


        Map<Long, CartItemEntity> cartById = cartItems.stream()
                .collect(Collectors.toMap(CartItemEntity::getCartItemId, Function.identity()));

        UserAddressEntity address = userAddressRepo
                .findByAddressIdAndUser_IdAndIsDeletedFalse(req.getShippingAddressId(), userId)
                .orElseThrow(() -> new RuntimeException("Shipping address not found"));

        OrderEntity order = OrderEntity.builder()
                .orderShippingFee(req.getOrderShippingFee())
                .orderTotalPrice(req.getOrderTotalPrice())
                .orderCreatedAt(LocalDate.now())
                .orderStatus(OrderStatus.PENDING_CONFIRMATION.getValue())
                .userEntity(UserEntity.builder().id(userId).build())
                .shippingAddress(address)
                .build();

        order = orderRepo.save(order);

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
            shopOrder.setShopOrderStatus(OrderStatus.PENDING_CONFIRMATION.getValue());
            shopOrder.setCreatedAt(LocalDateTime.now());
            shopOrder.setUpdatedAt(null);
            shopOrder.setOrderVoucher(orderVoucher);
            shopOrder.setShippingVoucher(shippingVoucher);

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

                    ProductEntity product = ci.getProduct();
                    if (product == null) throw new RuntimeException("CartItem has no product: " + ci.getCartItemId());

                    if (itemReq.getProductId() != null && !Objects.equals(product.getProductId(), itemReq.getProductId())) {
                        throw new RuntimeException("Product mismatch for cartItemId=" + ci.getCartItemId());
                    }

                    OrderItemEntity oi = OrderItemEntity.builder()
                            .orderItemQuantity(qty)
                            .orderItemPrice(price)
                            .orderItemTotalPrice(total)
                            .product(product)
                            .shopOrder(shopOrder)
                            .selectedValueIds(ci.getSelectedValueIds())
                            .variantText(ci.getVariantText())
                            .build();

                    orderItems.add(oi);
                }
            }

            shopOrder.setOrderItemEntities(orderItems);
            shopOrdersToSave.add(shopOrder);
        }

        if (req.getPayment() != null) {
            PaymentEntity payment = PaymentEntity.builder()
                    .paymentCode(req.getPayment().getPaymentCode())
                    .paymentMethod(req.getPayment().getPaymentMethod())
                    .pricePayment(req.getPayment().getPricePayment())
                    .description(req.getPayment().getDescription())
                    .paymentCreatedAt(LocalDate.now())
                    .build();

            order.setPayment(payment);
            payment.setOrder(order);
        }


        shopOrderRepo.saveAll(shopOrdersToSave);
        orderRepo.save(order);
        cartItemRepo.deleteAllByCart_UserEntity_IdAndCartItemIdIn(userId, cartItemIds);

        return orderMapper.toResponse(order);
    }

    @Override
    public List<OrderResponse> getOrderByUserId(Long userId) {
        List<OrderEntity>  order = orderRepo.findAllByUserEntity_Id(userId);
        return orderMapper.toResponseList(order);
    }

    @Override
    public List<ShopOrderResponse> getShopOrdersByShop(Long shopId) {
        List<ShopOrderEntity> shopOrder = shopOrderRepo.findAllByShop_ShopIdOrderByCreatedAtDesc(shopId);
        return shopOrderMapper.toResponseList(shopOrder);
    }

    @Override
    public List<ShopOrderResponse> getShopOrderByUserId(Long userId) {
        List<ShopOrderEntity> shopOrder = shopOrderRepo.findListShopOrderByUserId(userId);
        return shopOrderMapper.toResponseList(shopOrder);
    }

    @Override
    @Transactional
    public ShopOrderResponse updateShopOrderStatus(Long shopOrderId, Integer newStatus) {
        if (shopOrderId == null) throw new RuntimeException("shopOrderId is required");
        if (newStatus == null) throw new RuntimeException("newStatus is required");

        if (newStatus < OrderStatus.PENDING_CONFIRMATION.getValue() || newStatus > OrderStatus.RETURN_REFUND.getValue()) {
            throw new RuntimeException("Invalid status. Must be 0..6");
        }

        ShopOrderEntity shopOrder = shopOrderRepo.findById(shopOrderId)
                .orElseThrow(() -> new RuntimeException("ShopOrder not found: " + shopOrderId));

        shopOrder.setShopOrderStatus(newStatus);
        shopOrder.setUpdatedAt(LocalDateTime.now());

        ShopOrderEntity saved = shopOrderRepo.save(shopOrder);

        // ✅ [NEW] LOGIC CẬP NHẬT SỐ LƯỢNG ĐÃ BÁN (SOLD)
        // Nếu trạng thái mới là COMPLETED (giả sử số 4, bạn check lại Enum của bạn nhé)
        if (newStatus == 4) {
            // Duyệt qua tất cả item trong đơn đó
            List<OrderItemEntity> items = shopOrder.getOrderItemEntities();
            if (items != null) {
                for (OrderItemEntity item : items) {
                    Long productId = item.getProduct().getProductId();

                    // 1. Gọi Repo tính tổng sold (Status 4 = Completed)
                    Long totalSold = orderItemRepo.countTotalSoldByProductId(productId, 4);

                    // 2. Update vào Product
                    ProductEntity product = productRepo.findById(productId).orElse(null);
                    if (product != null) {
                        product.setSold(totalSold); // Đảm bảo Entity Product đã có field 'sold'
                        productRepo.save(product);
                    }
                }
            }
        }
        // ✅ [END NEW]

        return shopOrderMapper.toResponse(saved);
    }
}