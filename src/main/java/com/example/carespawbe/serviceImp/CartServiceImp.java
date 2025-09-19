package com.example.carespawbe.serviceImp;

import com.example.carespawbe.dto.request.CartItemRequest;
import com.example.carespawbe.dto.request.CartRequest;
import com.example.carespawbe.dto.response.CartResponse;
import com.example.carespawbe.entity.UserEntity;
import com.example.carespawbe.entity.shop.*;
import com.example.carespawbe.mapper.CartMapper;
import com.example.carespawbe.repository.UserRepository;
import com.example.carespawbe.repository.shop.*;
import com.example.carespawbe.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
public class CartServiceImp implements CartService {
    @Autowired
    private CartRepository cartRepository;
    @Autowired
    private CartItemRepository cartItemRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private VoucherRepository voucherRepository;
    @Autowired
    private CartMapper cartMapper;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ProductVarriantRepository productVarriantRepository;
    @Override
    public CartResponse createCart(CartRequest request) {
        try {
            UserEntity userEntity = userRepository.findById(request.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            VoucherEntity voucherEntity = request.getVoucherId() != null
                    ? voucherRepository.findVoucherEntitiesByVoucherId(request.getVoucherId())
                    : null;

            // tìm cart của user
            CartEntity cartEntity = cartRepository.findCartEntityByUserEntity_Id(request.getUserId());

            // nếu chưa có thì tạo mới
            if (cartEntity == null) {
                cartEntity = new CartEntity();
                cartEntity.setUserEntity(userEntity);
                cartEntity.setCreatedAt(request.getCreatedAt());
                cartEntity.setCartItemEntityList(new ArrayList<>());
            }

            cartEntity.setCartTotalPrice(request.getCartTotalPrice());
            cartEntity.setCartShippingFee(request.getCartShippingFee());
            cartEntity.setCartTotalCoinEarned(request.getCartTotalCoinEarned());
            cartEntity.setVoucher(voucherEntity);

            if (request.getCartItems() != null && !request.getCartItems().isEmpty()) {
                for (CartItemRequest itemReq : request.getCartItems()) {
                    ProductEntity product = productRepository.findProductEntityByProductId(itemReq.getProductId());

                    // tìm xem product này đã có trong cart chưa
                    Optional<CartItemEntity> existingItemOpt = cartEntity.getCartItemEntityList().stream()
                            .filter(ci -> ci.getProduct().getProductId().equals(product.getProductId()))
                            .findFirst();

                    if (existingItemOpt.isPresent()) {
                        // nếu đã có thì tăng số lượng
                        CartItemEntity existingItem = existingItemOpt.get();
                        existingItem.setCartItemQuantity(existingItem.getCartItemQuantity() + itemReq.getCartItemQuantity());
                        existingItem.setCartItemTotalPrice(
                                existingItem.getCartItemTotalPrice() + itemReq.getCartItemTotalPrice()
                        );
                        existingItem.setCartItemPrice(itemReq.getCartItemPrice()); // giá có thể update theo lần add mới
                    } else {
                        // nếu chưa có thì tạo mới cartItem
                        CartItemEntity cartItemEntity = new CartItemEntity();
                        cartItemEntity.setCart(cartEntity);
                        cartItemEntity.setCartItemPrice(itemReq.getCartItemPrice());
                        cartItemEntity.setCartItemOriginalPrice(itemReq.getCartItemOriginalPrice());
                        cartItemEntity.setCartItemQuantity(itemReq.getCartItemQuantity());
                        cartItemEntity.setCartItemTotalPrice(itemReq.getCartItemTotalPrice());
                        cartItemEntity.setFlashSale(itemReq.isFlashSale());
                        cartItemEntity.setProduct(product);
                        cartEntity.getCartItemEntityList().add(cartItemEntity);
                    }
                }
            }

            CartEntity savedCart = cartRepository.save(cartEntity);
            return cartMapper.toCartResponse(savedCart);

        } catch (Exception e) {
            throw new RuntimeException("Error creating cart", e);
        }
    }



    @Override
    public CartResponse updateCart(Long cartId, CartRequest request) {
        try {
            UserEntity userEntity = userRepository.findById(request.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            VoucherEntity voucherEntity = request.getVoucherId() != null
                    ? voucherRepository.findVoucherEntitiesByVoucherId(request.getVoucherId())
                    : null;

            CartEntity cartEntity = new CartEntity();
            cartEntity.setCartId(cartId);
            cartEntity.setUserEntity(userEntity);
            cartEntity.setCreatedAt(request.getCreatedAt());
            cartEntity.setCartTotalPrice(request.getCartTotalPrice());
            cartEntity.setCartShippingFee(request.getCartShippingFee());
            cartEntity.setCartTotalCoinEarned(request.getCartTotalCoinEarned());
            cartEntity.setVoucher(voucherEntity);

            if (request.getCartItems() != null && !request.getCartItems().isEmpty()) {
                for (CartItemRequest itemReq : request.getCartItems()) {
                    ProductEntity pr = productRepository.findProductEntityByProductId(itemReq.getProductId());
                    CartItemEntity cartItemEntity = new CartItemEntity();
                    cartItemEntity.setCart(cartEntity);
                    if (itemReq.getCartItemId() != null ) {
                        cartItemEntity.setCartItemId(itemReq.getCartItemId());
                    }
                    cartItemEntity.setCartItemPrice(itemReq.getCartItemPrice());
                    cartItemEntity.setCartItemOriginalPrice(itemReq.getCartItemOriginalPrice());
                    cartItemEntity.setCartItemQuantity(itemReq.getCartItemQuantity());
                    cartItemEntity.setCartItemTotalPrice(itemReq.getCartItemTotalPrice());
                    cartItemEntity.setFlashSale(itemReq.isFlashSale());
                    cartItemEntity.setProduct(pr);

//                    cartItemEntity.setProduct(productEntity);

                    cartEntity.getCartItemEntityList().add(cartItemEntity);
                }
            }

            CartEntity savedCart = cartRepository.save(cartEntity);
            return cartMapper.toCartResponse(savedCart);

        } catch (Exception e) {
            throw new RuntimeException("Error update cart", e);
        }
    }

    @Override
    public void deleteCart(Long cartItemId) {
        CartItemEntity cartItemEntity = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("cartItemId not found"));
        cartItemRepository.delete(cartItemEntity);
    }

    @Override
    public CartResponse getCartByUserId(Long userId) {
        CartEntity cartEntity = cartRepository.findCartEntityByUserEntity_Id(userId);
        if (cartEntity != null) {
            return cartMapper.toCartResponse(cartEntity);
        }
        return null;
    }
}