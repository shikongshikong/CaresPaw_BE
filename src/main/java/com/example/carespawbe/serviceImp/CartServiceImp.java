package com.example.carespawbe.serviceImp;

import com.example.carespawbe.dto.request.CartItemRequest;
import com.example.carespawbe.dto.request.CartRequest;
import com.example.carespawbe.dto.response.CartResponse;
import com.example.carespawbe.entity.UserEntity;
import com.example.carespawbe.entity.shop.CartEntity;
import com.example.carespawbe.entity.shop.CartItemEntity;
import com.example.carespawbe.entity.shop.ProductEntity;
import com.example.carespawbe.entity.shop.VoucherEntity;
import com.example.carespawbe.mapper.CartMapper;
import com.example.carespawbe.repository.UserRepository;
import com.example.carespawbe.repository.shop.CartItemRepository;
import com.example.carespawbe.repository.shop.CartRepository;
import com.example.carespawbe.repository.shop.ProductRepository;
import com.example.carespawbe.repository.shop.VoucherRepository;
import com.example.carespawbe.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    @Override
    public CartResponse createCart(CartRequest request) {
        try {
            UserEntity userEntity = userRepository.findById(request.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            VoucherEntity voucherEntity = request.getVoucherId() != null
                    ? voucherRepository.findVoucherEntitiesByVoucherId(request.getVoucherId())
                    : null;

            CartEntity cartEntity = new CartEntity();
            cartEntity.setUserEntity(userEntity);
            cartEntity.setCreatedAt(request.getCreatedAt());
            cartEntity.setCartTotalPrice(request.getCartTotalPrice());
            cartEntity.setCartShippingFee(request.getCartShippingFee());
            cartEntity.setCartTotalCoinEarned(request.getCartTotalCoinEarned());
            cartEntity.setVoucher(voucherEntity);

            if (request.getCartItems() != null && !request.getCartItems().isEmpty()) {
                for (CartItemRequest itemReq : request.getCartItems()) {
                    ProductEntity productEntity = productRepository.findById(itemReq.getProductId())
                            .orElseThrow(() -> new RuntimeException("Product not found"));

                    CartItemEntity cartItemEntity = new CartItemEntity();
                    cartItemEntity.setCart(cartEntity);
                    cartItemEntity.setCartItemPrice(itemReq.getCartItemPrice());
                    cartItemEntity.setCartItemOriginalPrice(itemReq.getCartItemOriginalPrice());
                    cartItemEntity.setCartItemQuantity(itemReq.getCartItemQuantity());
                    cartItemEntity.setCartItemTotalPrice(itemReq.getCartItemTotalPrice());
                    cartItemEntity.setFlashSale(itemReq.isFlashSale());
                    cartItemEntity.setProduct(productEntity);

                    cartEntity.getCartItemEntityList().add(cartItemEntity);
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
                    ProductEntity productEntity = productRepository.findById(itemReq.getProductId())
                            .orElseThrow(() -> new RuntimeException("Product not found"));

                    CartItemEntity cartItemEntity = new CartItemEntity();
                    cartItemEntity.setCart(cartEntity);
                    cartItemEntity.setCartItemPrice(itemReq.getCartItemPrice());
                    cartItemEntity.setCartItemOriginalPrice(itemReq.getCartItemOriginalPrice());
                    cartItemEntity.setCartItemQuantity(itemReq.getCartItemQuantity());
                    cartItemEntity.setCartItemTotalPrice(itemReq.getCartItemTotalPrice());
                    cartItemEntity.setFlashSale(itemReq.isFlashSale());
                    cartItemEntity.setProduct(productEntity);

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
    public void deleteCart(Long cartId) {

    }

    @Override
    public CartResponse getCartByUserId(Long userId) {
        return null;
    }
}