//package com.example.carespawbe.serviceImp;
//
//import com.example.carespawbe.dto.request.CartRequest;
//import com.example.carespawbe.dto.response.CartResponse;
//import com.example.carespawbe.entity.UserEntity;
//import com.example.carespawbe.entity.shop.CartEntity;
//import com.example.carespawbe.entity.shop.VoucherEntity;
//import com.example.carespawbe.mapper.CartMapper;
//import com.example.carespawbe.repository.UserRepository;
//import com.example.carespawbe.repository.shop.CartRepository;
//import com.example.carespawbe.repository.shop.VoucherRepository;
//import com.example.carespawbe.service.CartService;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.time.LocalDate;
//import java.util.List;
//
//@Service
//@RequiredArgsConstructor
//public class CartServiceImp implements CartService {
//
//    @Autowired
//    private CartRepository cartRepository;
//    @Autowired
//    private UserRepository userRepository;
//    @Autowired
//    private VoucherRepository voucherRepository;
//    @Autowired
//    private CartMapper cartMapper;
//
//    @Override
//    public CartResponse createCart(CartRequest request) {
//        UserEntity user = userRepository.findById(request.getUserId())
//                .orElseThrow(() -> new RuntimeException("User not found"));
//
//        VoucherEntity voucher = null;
//        if (request.getVoucherId() != null) {
//            voucher = voucherRepository.findById(request.getVoucherId())
//                    .orElseThrow(() -> new RuntimeException("Voucher not found"));
//        }
//
//        CartEntity cart = CartEntity.builder()
//                .CartTotalPrice(request.getCartTotalPrice())
//                .CartShippingFee(request.getCartShippingFee())
//                .CartTotalCoinEarned(request.getCartTotalCoinEarned())
//                .createdAt(LocalDate.now())
//                .updatedAt(LocalDate.now())
//                .user(user)
//                .voucher(voucher)
//                .build();
//
//        return cartMapper.toCartResponse(cartRepository.save(cart));
//    }
//
//    @Override
//    public CartResponse updateCart(Long cartId, CartRequest request) {
//        CartEntity cart = cartRepository.findById(cartId)
//                .orElseThrow(() -> new RuntimeException("Cart not found"));
//
//        cart.setCartTotalPrice(request.getCartTotalPrice());
//        cart.setCartShippingFee(request.getCartShippingFee());
//        cart.setCartTotalCoinEarned(request.getCartTotalCoinEarned());
//        cart.setUpdatedAt(LocalDate.now());
//
//        return cartMapper.toCartResponse(cartRepository.save(cart));
//    }
//
//    @Override
//    public void deleteCart(Long cartId) {
//        cartRepository.deleteById(cartId);
//    }
//
//    @Override
//    public CartResponse getCartByUserId(Long userId) {
//        CartEntity cart = cartRepository.findByUserUserId(userId)
//                .orElseThrow(() -> new RuntimeException("Cart not found for user"));
//        return cartMapper.toCartResponse(cart);
//    }
//
////    @Override
////    public List<CartResponse> getAllCarts() {
////        return cartRepository.findAll()
////                .stream()
////                .map(cartMapper::toResponse)
////                .toList();
////    }
//}
