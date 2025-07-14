//package com.example.carespawbe.serviceImp;
//
//import com.example.carespawbe.dto.request.CartItemRequest;
//import com.example.carespawbe.dto.response.CartItemResponse;
//import com.example.carespawbe.entity.shop.*;
//import com.example.carespawbe.mapper.CartItemMapper;
//import com.example.carespawbe.repository.shop.*;
//import com.example.carespawbe.service.CartItemService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//import java.util.Optional;
//
//@Service
//@RequiredArgsConstructor
//public class CartItemServiceImp implements CartItemService {
//
//    private final CartItemRepository cartItemRepository;
//    private final CartRepository cartRepository;
//    private final ProductRepository productRepository;
//    private final ProductVarriantRepository productVarriantRepository;
//    private final CartItemMapper cartItemMapper;
//
//    @Override
//    public CartItemResponse addCartItem(CartItemRequest request) {
//        CartEntity cart = cartRepository.findById(request.getCartId())
//                .orElseThrow(() -> new RuntimeException("Cart not found"));
//        ProductEntity product = productRepository.findById(request.getProductId())
//                .orElseThrow(() -> new RuntimeException("Product not found"));
//        ProductVarriantEntity variant = productVarriantRepository.findById(request.getProductVariantId())
//                .orElseThrow(() -> new RuntimeException("Product variant not found"));
//
//        CartItemEntity entity = CartItemEntity.builder()
//                .cart(cart)
//                .product(product)
//                .productVariant(variant)
//                .cartItemPrice(request.getCartItemPrice())
//                .cartItemOriginalPrice(request.getCartItemOriginalPrice())
//                .cartItemQuantity(request.getCartItemQuantity())
//                .cartItemTotalPrice(request.getCartItemTotalPrice())
//                .isFlashSale(request.isFlashSale())
//                .build();
//
//        return cartItemMapper.toResponse(cartItemRepository.save(entity));
//    }
//
//    @Override
//    public CartItemResponse updateCartItem(Long cartItemId, CartItemRequest request) {
//        CartItemEntity entity = cartItemRepository.findById(cartItemId)
//                .orElseThrow(() -> new RuntimeException("Cart item not found"));
//
//        entity.setCartItemPrice(request.getCartItemPrice());
//        entity.setFlashSale(request.isFlashSale());
//
//        return cartItemMapper.toResponse(cartItemRepository.save(entity));
//    }
//
//    @Override
//    public void deleteCartItem(Long cartItemId) {
//        cartItemRepository.deleteById(cartItemId);
//    }
//
//    @Override
//    public List<CartItemResponse> getCartItemsByCartId(Long cartId) {
//        List<CartItemEntity> items = cartItemRepository.findByCart_CartId(cartId);
//        return items.stream().map(cartItemMapper::toResponse).toList();
//    }
//
//    @Override
//    public CartItemResponse getCartItemById(Long cartItemId) {
//        CartItemEntity entity = cartItemRepository.findById(cartItemId)
//                .orElseThrow(() -> new RuntimeException("Cart item not found"));
//        return cartItemMapper.toResponse(entity);
//    }
//}
