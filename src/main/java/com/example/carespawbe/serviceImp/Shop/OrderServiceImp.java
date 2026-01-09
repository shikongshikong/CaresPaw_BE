package com.example.carespawbe.serviceImp.Shop;

import com.example.carespawbe.dto.Notification.NotificationCreateRequest;
import com.example.carespawbe.dto.Shop.RevenueTimelineDTO;
import com.example.carespawbe.dto.Shop.UserProductOrderTimeDTO;
import com.example.carespawbe.dto.Shop.request.OrderItemRequest;
import com.example.carespawbe.dto.Shop.request.OrderRequest;
import com.example.carespawbe.dto.Shop.request.ShopOrderRequest;
import com.example.carespawbe.dto.Shop.response.OrderResponse;
import com.example.carespawbe.dto.Shop.response.ShopOrderResponse;
import com.example.carespawbe.entity.Auth.UserAddressEntity;
import com.example.carespawbe.entity.Auth.UserEntity;
import com.example.carespawbe.entity.Shop.*;
import com.example.carespawbe.enums.NotificationType;
import com.example.carespawbe.enums.OrderStatus;
import com.example.carespawbe.mapper.Shop.OrderMapper;
import com.example.carespawbe.mapper.Shop.ShopOrderMapper;
import com.example.carespawbe.repository.Auth.UserAddressRepository;
import com.example.carespawbe.repository.Shop.*;
import com.example.carespawbe.service.Notification.NotificationService;
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

    @Autowired
    private CartItemRepository cartItemRepo;
    @Autowired
    private OrderRepository orderRepo;
    @Autowired
    private ShopOrderRepository shopOrderRepo;
    @Autowired
    private VoucherRepository voucherRepo;
    @Autowired
    private ShopRepository shopRepo;
    @Autowired
    private ProductRepository productRepo;
    @Autowired
    private UserAddressRepository userAddressRepo;
    @Autowired
    private OrderItemRepository orderItemRepository;
    @Autowired
    private NotificationService notificationService;


    // ✅ NEW: cần SKU repo để check/trừ/hoàn stock + update sold
    @Autowired private ProductSkuRepository productSkuRepo;

    @Autowired private OrderMapper orderMapper;
    @Autowired private ShopOrderMapper shopOrderMapper;

    @Override
    @Transactional
    public OrderResponse checkout(Long userId, OrderRequest req) {
        if (userId == null) throw new RuntimeException("userId is required");
        if (req == null || req.getShopOrders() == null || req.getShopOrders().isEmpty()) {
            throw new RuntimeException("shopOrders is empty");
        }

        // ===== 1) LẤY cartItemIds từ request =====
        List<Long> cartItemIds = req.getShopOrders().stream()
                .flatMap(so -> (so.getOrderItems() == null ? List.<OrderItemRequest>of() : so.getOrderItems()).stream())
                .map(OrderItemRequest::getCartItemId)
                .filter(Objects::nonNull)
                .distinct()
                .toList();

        if (cartItemIds.isEmpty()) throw new RuntimeException("No items to checkout");

        // ===== 2) LẤY cart items thuộc user =====
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

        // ===== 3) LOAD ADDRESS =====
        UserAddressEntity address = userAddressRepo
                .findByAddressIdAndUser_IdAndIsDeletedFalse(req.getShippingAddressId(), userId)
                .orElseThrow(() -> new RuntimeException("Shipping address not found"));

        // ===== 4) ✅ RECALCULATE TOTAL BACKEND (KHÔNG TIN FE) =====
        double backendTotalShippingFee = 0.0;
        double backendTotalProductAmount = 0.0;

        for (ShopOrderRequest soReq : req.getShopOrders()) {
            double shopShippingFee = soReq.getShippingFee() == null ? 0.0 : soReq.getShippingFee();
            backendTotalShippingFee += shopShippingFee;

            if (soReq.getOrderItems() != null) {
                for (OrderItemRequest itemReq : soReq.getOrderItems()) {
                    CartItemEntity ci = cartById.get(itemReq.getCartItemId());
                    if (ci == null) continue;

                    int qty = (itemReq.getOrderItemQuantity() == null || itemReq.getOrderItemQuantity() < 1)
                            ? (ci.getCartItemQuantity() == null ? 1 : ci.getCartItemQuantity())
                            : itemReq.getOrderItemQuantity();

                    ProductSkuEntity sku = ci.getProductSku();
                    if (sku == null) throw new RuntimeException("CartItem has no SKU: " + ci.getCartItemId());

                    double unitPrice = 0.0;
                    if (sku.getPrice() != null) unitPrice = sku.getPrice().doubleValue();
                    else if (ci.getCartItemPrice() != null) unitPrice = ci.getCartItemPrice();

                    backendTotalProductAmount += unitPrice * qty;
                }
            }
        }

        double backendOrderTotal = backendTotalProductAmount + backendTotalShippingFee;

        // ===== 5) CREATE ORDER =====
        OrderEntity order = OrderEntity.builder()
                .orderShippingFee(backendTotalShippingFee)
                .orderTotalPrice(backendOrderTotal)
                .orderCreatedAt(LocalDate.now())
                .orderStatus(OrderStatus.PENDING_CONFIRMATION.getValue())
                .userEntity(UserEntity.builder().id(userId).build())
                .shippingAddress(address)
                .build();

        order = orderRepo.save(order);

        List<ShopOrderEntity> shopOrdersToSave = new ArrayList<>();

        // ===== 6) ✅ CHECK & TRỪ STOCK THEO SKU (TRONG TRANSACTION) =====
        Map<Long, Integer> skuNeedQty = new HashMap<>();
        for (ShopOrderRequest soReq : req.getShopOrders()) {
            if (soReq.getOrderItems() == null) continue;

            for (OrderItemRequest itemReq : soReq.getOrderItems()) {
                CartItemEntity ci = cartById.get(itemReq.getCartItemId());
                if (ci == null) continue;

                ProductSkuEntity sku = ci.getProductSku();
                if (sku == null) throw new RuntimeException("CartItem has no SKU: " + ci.getCartItemId());

                int qty = (itemReq.getOrderItemQuantity() == null || itemReq.getOrderItemQuantity() < 1)
                        ? (ci.getCartItemQuantity() == null ? 1 : ci.getCartItemQuantity())
                        : itemReq.getOrderItemQuantity();

                skuNeedQty.merge(sku.getProductSkuId(), qty, Integer::sum);
            }
        }

        List<ProductSkuEntity> skus = productSkuRepo.findAllByProductSkuIdIn(new ArrayList<>(skuNeedQty.keySet()));
        Map<Long, ProductSkuEntity> skuMap = skus.stream()
                .collect(Collectors.toMap(ProductSkuEntity::getProductSkuId, Function.identity()));

        for (Map.Entry<Long, Integer> e : skuNeedQty.entrySet()) {
            Long skuId = e.getKey();
            int need = e.getValue();

            ProductSkuEntity sku = skuMap.get(skuId);
            if (sku == null) throw new RuntimeException("SKU not found: " + skuId);

            Integer stock = sku.getStock() == null ? 0 : sku.getStock();
            if (need > stock) {
                throw new RuntimeException("Not enough stock for SKU " + skuId + ". Need=" + need + ", Stock=" + stock);
            }
        }

        for (Map.Entry<Long, Integer> e : skuNeedQty.entrySet()) {
            Long skuId = e.getKey();
            int need = e.getValue();

            ProductSkuEntity sku = skuMap.get(skuId);
            int stock = sku.getStock() == null ? 0 : sku.getStock();
            sku.setStock(stock - need);
        }
        productSkuRepo.saveAll(skus);

        // ===== 7) BUILD SHOP ORDERS + ORDER ITEMS =====
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
                            ? (ci.getCartItemQuantity() == null ? 1 : ci.getCartItemQuantity())
                            : itemReq.getOrderItemQuantity();

                    ProductSkuEntity sku = ci.getProductSku();
                    if (sku == null) throw new RuntimeException("CartItem has no SKU: " + ci.getCartItemId());

                    double unitPrice = 0.0;
                    if (sku.getPrice() != null) unitPrice = sku.getPrice().doubleValue();
                    else if (ci.getCartItemPrice() != null) unitPrice = ci.getCartItemPrice();

                    double total = unitPrice * qty;

                    OrderItemEntity oi = OrderItemEntity.builder()
                            .orderItemQuantity(qty)
                            .orderItemPrice(unitPrice)
                            .orderItemTotalPrice(total)
                            .productSku(sku)
                            .shopOrder(shopOrder)
                            .skuCode(ci.getSkuCode() != null ? ci.getSkuCode() : sku.getSkuCode())
                            .variantText(ci.getVariantText())
                            .productName(sku.getProduct().getProductName())
                            .build();

                    orderItems.add(oi);
                }
            }

            shopOrder.setOrderItemEntities(orderItems);
            shopOrdersToSave.add(shopOrder);
        }

        shopOrderRepo.saveAll(shopOrdersToSave);
        orderRepo.save(order);

        cartItemRepo.deleteAllByCart_UserEntity_IdAndCartItemIdIn(userId, cartItemIds);

        notificationService.create(NotificationCreateRequest.builder()
                .userId(userId)
                .actorId(null)
                .type(NotificationType.SHOP)
                .title("Order placed successfully")
                .message("Your order #" + order.getOrderId() + " has been created.")
                .link("/shop/orders/" + order.getOrderId())
                .entityType("ORDER")
                .entityId(order.getOrderId())
                .build());

        return orderMapper.toResponse(order);
    }

    @Override
    public List<OrderResponse> getOrderByUserId(Long userId) {
        List<OrderEntity> order = orderRepo.findAllByUserEntity_Id(userId);
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

        if (newStatus < OrderStatus.PENDING_CONFIRMATION.getValue()
                || newStatus > OrderStatus.RETURN_REFUND.getValue()) {
            throw new RuntimeException("Invalid status. Must be 0..6");
        }

        ShopOrderEntity shopOrder = shopOrderRepo.findById(shopOrderId)
                .orElseThrow(() -> new RuntimeException("ShopOrder not found: " + shopOrderId));

        Integer oldStatus = shopOrder.getShopOrderStatus();

        final int CANCELLED_STATUS = OrderStatus.CANCELLED.getValue(); // sửa nếu enum khác
        final int DONE_STATUS = 4; // bạn đang dùng 4

        boolean isCancellingNow = (newStatus == CANCELLED_STATUS) && (oldStatus == null || !oldStatus.equals(CANCELLED_STATUS));
        boolean isDoneNow = (newStatus == DONE_STATUS) && (oldStatus == null || !oldStatus.equals(DONE_STATUS));

        // ✅ HOÀN STOCK KHI HUỶ
        if (isCancellingNow) {
            List<OrderItemEntity> items = shopOrder.getOrderItemEntities();
            if (items != null && !items.isEmpty()) {
                Map<Long, Integer> restoreMap = new HashMap<>();
                for (OrderItemEntity it : items) {
                    if (it.getProductSku() == null) continue;
                    Long skuId = it.getProductSku().getProductSkuId();
                    int qty = it.getOrderItemQuantity() == null ? 0 : it.getOrderItemQuantity();
                    restoreMap.merge(skuId, qty, Integer::sum);
                }

                if (!restoreMap.isEmpty()) {
                    List<ProductSkuEntity> skus = productSkuRepo.findAllByProductSkuIdIn(new ArrayList<>(restoreMap.keySet()));
                    for (ProductSkuEntity sku : skus) {
                        int cur = sku.getStock() == null ? 0 : sku.getStock();
                        int add = restoreMap.getOrDefault(sku.getProductSkuId(), 0);
                        sku.setStock(cur + add);
                    }
                    productSkuRepo.saveAll(skus);
                }
            }
        }

        // ✅ SET STATUS
        shopOrder.setShopOrderStatus(newStatus);
        shopOrder.setUpdatedAt(LocalDateTime.now());
        ShopOrderEntity saved = shopOrderRepo.save(shopOrder);

        // ✅ SYNC parent Order status based on ALL ShopOrders
        if (saved.getOrder() != null) {
            syncParentOrderStatus(saved.getOrder().getOrderId());
        }

        // ===== NOTIFY USER (owner of parent order) =====
        Long buyerId = null;
        if (saved.getOrder() != null && saved.getOrder().getUserEntity() != null) {
            buyerId = saved.getOrder().getUserEntity().getId();
        }
        if (buyerId != null) {
            notificationService.create(NotificationCreateRequest.builder()
                    .userId(buyerId)
                    .actorId(null) // nếu bạn muốn actor là shopOwnerId thì phải lấy từ shop/user
                    .type(NotificationType.SHOP)
                    .title("Order update from shop")
                    .message("Your order status changed to: " + statusText(newStatus))
                    .link("/shop/orders/" + saved.getOrder().getOrderId())
                    .entityType("SHOP_ORDER")
                    .entityId(saved.getShopOrderId())
                    .build());
        }

        // ✅ DONE: cộng sold cho SKU + sync product.sold
        if (isDoneNow) {
            List<OrderItemEntity> items = shopOrder.getOrderItemEntities();
            if (items != null && !items.isEmpty()) {

                Map<Long, Long> skuQtyMap = new HashMap<>();
                Set<Long> productIds = new HashSet<>();

                for (OrderItemEntity it : items) {
                    ProductSkuEntity sku = it.getProductSku();
                    if (sku == null) continue;

                    Long skuId = sku.getProductSkuId();
                    long qty = it.getOrderItemQuantity() == null ? 0L : it.getOrderItemQuantity().longValue();
                    if (qty <= 0) continue;

                    skuQtyMap.merge(skuId, qty, Long::sum);

                    if (sku.getProduct() != null && sku.getProduct().getProductId() != null) {
                        productIds.add(sku.getProduct().getProductId());
                    }
                }

                // 1) cộng sold vào từng SKU (atomic)
                for (Map.Entry<Long, Long> e : skuQtyMap.entrySet()) {
                    productSkuRepo.increaseSold(e.getKey(), e.getValue());
                }

                // 2) sync product.sold = SUM sku.sold active
                for (Long pid : productIds) {
                    productRepo.syncProductSoldFromSkus(pid);
                }
            }
        }

        return shopOrderMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public ShopOrderResponse getShopOrderDetail(Long id) {
        ShopOrderEntity shopOrder = shopOrderRepo.findDetail(id)
                .orElseThrow(() -> new RuntimeException("ShopOrder not found: " + id));

        return shopOrderMapper.toResponse(shopOrder);
    }

    @Override
    public List<UserProductOrderTimeDTO> findUserProductOrderTimes() {
        return orderItemRepository.findUserProductOrderTimes();
    }

    private String statusText(Integer st) {
        if (st == null) return "Unknown";
        if (st.equals(OrderStatus.PENDING_CONFIRMATION.getValue())) return "Pending confirmation";
        if (st.equals(OrderStatus.AWAITING_PICKUP.getValue())) return "Confirmed";
        if (st.equals(OrderStatus.SHIPPING.getValue())) return "Packing";
        if (st.equals(OrderStatus.AWAITING_DELIVERY.getValue())) return "Shipping";
        if (st.equals(OrderStatus.COMPLETED.getValue())) return "Delivered";
        if (st.equals(OrderStatus.CANCELLED.getValue())) return "Cancelled";
        if (st.equals(OrderStatus.RETURN_REFUND.getValue())) return "Return/Refund";
        return "Updated";
    }

    private void syncParentOrderStatus(Long orderId) {
        List<ShopOrderEntity> shopOrders = shopOrderRepo.findAllByOrder_OrderId(orderId);
        if (shopOrders == null || shopOrders.isEmpty()) return;

        boolean allCompleted = shopOrders.stream()
                .allMatch(so -> so.getShopOrderStatus() == OrderStatus.COMPLETED.getValue());

        boolean allCancelled = shopOrders.stream()
                .allMatch(so -> so.getShopOrderStatus() == OrderStatus.CANCELLED.getValue());

        if (allCompleted) {
            orderRepo.updateOrderStatus(orderId, OrderStatus.COMPLETED.getValue(), LocalDate.now());
        } else if (allCancelled) {
            orderRepo.updateOrderStatus(orderId, OrderStatus.CANCELLED.getValue(), LocalDate.now());
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<RevenueTimelineDTO> getRevenueTimeline(Long shopId, int monthsBack, int monthsForward) {
        if (shopId == null) throw new RuntimeException("shopId is required");

        LocalDate now = LocalDate.now();
        LocalDate start = now.minusMonths(monthsBack).withDayOfMonth(1);
        LocalDate endExclusive = now.plusMonths(monthsForward + 1).withDayOfMonth(1); // exclusive

        int COMPLETED = OrderStatus.COMPLETED.getValue();

        List<Object[]> rows = orderItemRepository.revenueByMonthForShopCompleted(
                shopId, COMPLETED, start, endExclusive
        );

        Map<String, Double> actualMap = new HashMap<>();
        for (Object[] r : rows) {
            int y = ((Number) r[0]).intValue();
            int m = ((Number) r[1]).intValue();
            double revenue = ((Number) r[2]).doubleValue();
            actualMap.put(y + "-" + m, revenue);
        }

        List<RevenueTimelineDTO> result = new ArrayList<>();
        LocalDate cursor = start;

        while (cursor.isBefore(endExclusive)) {
            int y = cursor.getYear();
            int m = cursor.getMonthValue();
            Double actual = actualMap.getOrDefault(y + "-" + m, 0.0);

            // Forecast: để null hoặc sample (tuỳ bạn)
            Double forecast = null;

            result.add(new RevenueTimelineDTO(y, m, actual, forecast));
            cursor = cursor.plusMonths(1);
        }

        return result;
    }

}
