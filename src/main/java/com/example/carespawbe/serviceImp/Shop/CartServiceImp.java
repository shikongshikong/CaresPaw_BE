package com.example.carespawbe.serviceImp.Shop;

import com.example.carespawbe.dto.Shop.request.CartItemRequest;
import com.example.carespawbe.dto.Shop.response.CartResponse;
import com.example.carespawbe.entity.Auth.UserEntity;
import com.example.carespawbe.entity.Shop.CartEntity;
import com.example.carespawbe.entity.Shop.CartItemEntity;
import com.example.carespawbe.entity.Shop.ProductSkuEntity;
import com.example.carespawbe.entity.Shop.ProductSkuValueEntity;
import com.example.carespawbe.mapper.Shop.CartMapper;
import com.example.carespawbe.repository.Auth.UserRepository;
import com.example.carespawbe.repository.Shop.CartItemRepository;
import com.example.carespawbe.repository.Shop.CartRepository;
import com.example.carespawbe.repository.Shop.ProductSkuRepository;
import com.example.carespawbe.repository.Shop.ProductSkuValueRepository;
import com.example.carespawbe.service.Shop.CartService;
import com.example.carespawbe.service.Shop.ProductSkuService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CartServiceImp implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final ProductSkuRepository productSkuRepository;
    private final ProductSkuValueRepository productSkuValueRepository;
    private final CartMapper cartMapper;

    @Autowired
    private ProductSkuService productSkuService;

    @Override
    @Transactional
    public CartResponse getOrCreateCartByUserId(Long userId) {
        if (userId == null) throw new RuntimeException("userId is required");

        CartEntity cart = cartRepository.findCartEntityByUserEntity_Id(userId);
        if (cart != null) return cartMapper.toCartResponse(cart);

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found: " + userId));

        CartEntity c = CartEntity.builder()
                .userEntity(user)
                .createdAt(LocalDate.now())
                .updatedAt(LocalDate.now())
                .cartTotalPrice(0.0)
                .build();

        return cartMapper.toCartResponse(cartRepository.save(c));
    }

    @Override
    public CartResponse getCartByUserId(Long userId) {
        if (userId == null) throw new RuntimeException("userId is required");

        CartEntity cart = cartRepository.findCartEntityByUserEntity_Id(userId);
        return cart == null ? null : cartMapper.toCartResponse(cart);
    }

    @Override
    @Transactional
    public void clearCart(Long cartId) {
        if (cartId == null) throw new RuntimeException("cartId is required");

        CartEntity cart = cartRepository.findCartEntityByCartId(cartId);
        if (cart == null) throw new RuntimeException("Cart not found: " + cartId);

        cartItemRepository.deleteAllByCart_CartId(cartId);

        cart.setCartTotalPrice(0.0);
        cart.setUpdatedAt(LocalDate.now());
        cartRepository.save(cart);
    }

    @Override
    @Transactional
    public void deleteCart(Long cartId) {
        if (cartId == null) throw new RuntimeException("cartId is required");

        CartEntity cart = cartRepository.findCartEntityByCartId(cartId);
        if (cart == null) throw new RuntimeException("Cart not found: " + cartId);

        cartRepository.delete(cart);
    }

    @Override
    @Transactional
    public CartResponse addItem(Long userId, CartItemRequest request) {

        if (userId == null) throw new RuntimeException("userId is required");
        if (request == null) throw new RuntimeException("request is required");
        if (request.getProductSkuId() == null) throw new RuntimeException("productSkuId is required");
        if (request.getCartItemQuantity() == null || request.getCartItemQuantity() <= 0)
            throw new RuntimeException("cartItemQuantity must be > 0");

        CartEntity cart = cartRepository.findByUserEntity_Id(userId)
                .orElseGet(() -> cartRepository.save(
                        CartEntity.builder()
                                .userEntity(userRepository.findById(userId).orElseThrow())
                                .createdAt(LocalDate.now())
                                .updatedAt(LocalDate.now())
                                .cartTotalPrice(0.0)
                                .build()
                ));

        ProductSkuEntity sku = productSkuRepository.findById(request.getProductSkuId())
                .orElseThrow(() -> new RuntimeException("SKU not found: " + request.getProductSkuId()));

        if (Boolean.FALSE.equals(sku.getIsActive()))
            throw new RuntimeException("SKU is inactive");

        if (sku.getStock() != null && sku.getStock() < request.getCartItemQuantity())
            throw new RuntimeException("Not enough stock");

        CartItemEntity item = cartItemRepository
                .findByCart_CartIdAndProductSku_ProductSkuId(cart.getCartId(), sku.getProductSkuId())
                .orElse(null);

        if (item == null) {
            item = CartItemEntity.builder()
                    .cart(cart)
                    .productSku(sku)
                    .cartItemQuantity(request.getCartItemQuantity())
                    .cartItemPrice(sku.getPrice() == null ? 0.0 : sku.getPrice().doubleValue())
                    .cartItemTotalPrice(
                            (sku.getPrice() == null ? 0.0 : sku.getPrice().doubleValue()) * request.getCartItemQuantity()
                    )
                    .skuCode(sku.getSkuCode())
                    .variantText(buildVariantTextFromSku(sku.getProductSkuId()))
                    .build();
        } else {
            int newQty = item.getCartItemQuantity() + request.getCartItemQuantity();

            if (sku.getStock() != null && newQty > sku.getStock())
                throw new RuntimeException("Not enough stock");

            item.setCartItemQuantity(newQty);
            item.setCartItemTotalPrice(newQty * item.getCartItemPrice());
        }

        cartItemRepository.save(item);
        recalcCartTotal(cart.getCartId());

        return cartMapper.toCartResponse(cartRepository.findById(cart.getCartId()).orElseThrow());
    }

    @Override
    @Transactional
    public CartResponse updateItem(Long userId, Long cartItemId, CartItemRequest request) {

        if (userId == null) throw new RuntimeException("userId is required");
        if (cartItemId == null) throw new RuntimeException("cartItemId is required");
        if (request == null) throw new RuntimeException("request is required");

        CartEntity cart = cartRepository.findCartEntityByUserEntity_Id(userId);
        if (cart == null) throw new RuntimeException("Cart not found for userId=" + userId);

        CartItemEntity item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("CartItem not found: " + cartItemId));

        if (!Objects.equals(item.getCart().getCartId(), cart.getCartId()))
            throw new RuntimeException("CartItem does not belong to this user");

        ProductSkuEntity sku = item.getProductSku();

        if (request.getProductSkuId() != null) {
            sku = productSkuRepository.findById(request.getProductSkuId())
                    .orElseThrow(() -> new RuntimeException("SKU not found: " + request.getProductSkuId()));
            item.setProductSku(sku);
            item.setSkuCode(sku.getSkuCode());
            item.setVariantText(buildVariantTextFromSku(sku.getProductSkuId()));
        }

        if (request.getCartItemQuantity() != null) {
            int qty = request.getCartItemQuantity();
            if (qty <= 0) throw new RuntimeException("cartItemQuantity must be > 0");

            if (sku.getStock() != null && qty > sku.getStock())
//                throw new RuntimeException("Not enough stock");
                return cartMapper.toCartResponse(cart);
            item.setCartItemQuantity(qty);
        }

        double price = sku.getPrice() == null ? 0.0 : sku.getPrice().doubleValue();
        item.setCartItemPrice(price);
        item.setCartItemTotalPrice(item.getCartItemQuantity() * price);

        cartItemRepository.save(item);
        recalcCartTotal(cart.getCartId());

        return cartMapper.toCartResponse(cartRepository.findCartEntityByCartId(cart.getCartId()));
    }

    @Override
    @Transactional
    public CartResponse removeItem(Long userId, Long cartItemId) {

        if (userId == null) throw new RuntimeException("userId is required");
        if (cartItemId == null) throw new RuntimeException("cartItemId is required");

        CartEntity cart = cartRepository.findCartEntityByUserEntity_Id(userId);
        if (cart == null) throw new RuntimeException("Cart not found for userId=" + userId);

        CartItemEntity item = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("CartItem not found: " + cartItemId));

        if (!Objects.equals(item.getCart().getCartId(), cart.getCartId()))
            throw new RuntimeException("CartItem does not belong to this user");

        cartItemRepository.delete(item);
        recalcCartTotal(cart.getCartId());

        return cartMapper.toCartResponse(cartRepository.findCartEntityByCartId(cart.getCartId()));
    }

    private void recalcCartTotal(Long cartId) {
        List<CartItemEntity> items = cartItemRepository.findAllByCart_CartId(cartId);

        double total = items.stream()
                .mapToDouble(i -> i.getCartItemTotalPrice() == null ? 0.0 : i.getCartItemTotalPrice())
                .sum();

        CartEntity cart = cartRepository.findCartEntityByCartId(cartId);
        cart.setCartTotalPrice(total);
        cart.setUpdatedAt(LocalDate.now());
        cartRepository.save(cart);
    }

    private String buildVariantTextFromSku(Long skuId) {
        List<ProductSkuValueEntity> maps = productSkuValueRepository.findByProductSku_ProductSkuId(skuId);

        return maps.stream()
                .sorted((a, b) -> a.getVarriant().getVarriantName()
                        .compareToIgnoreCase(b.getVarriant().getVarriantName()))
                .map(m -> m.getVarriant().getVarriantName() + ": " + m.getVarriantValue().getValueName())
                .reduce((x, y) -> x + " | " + y)
                .orElse("");
    }
}
