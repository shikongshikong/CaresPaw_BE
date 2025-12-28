package com.example.carespawbe.serviceImp.Shop;

import com.example.carespawbe.dto.Shop.request.CartItemRequest;
import com.example.carespawbe.dto.Shop.request.CartRequest;
import com.example.carespawbe.dto.Shop.response.CartItemResponse;
import com.example.carespawbe.dto.Shop.response.CartResponse;
import com.example.carespawbe.dto.Shop.response.ImageProductResponse;
import com.example.carespawbe.dto.Shop.response.ProductVarriantResponse;
import com.example.carespawbe.entity.Auth.UserEntity;
import com.example.carespawbe.entity.Shop.*;
import com.example.carespawbe.mapper.Shop.CartItemMapper;
import com.example.carespawbe.mapper.Shop.CartMapper;
import com.example.carespawbe.mapper.Shop.ImageProductMapper;
import com.example.carespawbe.mapper.Shop.ProductVarriantMapper;
import com.example.carespawbe.repository.Auth.UserRepository;
import com.example.carespawbe.repository.Shop.*;
import com.example.carespawbe.service.Shop.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
    @Autowired
    protected ProductVarriantMapper productVarriantMapper;
    @Autowired
    private ImageProductRepository imageProductRepository;
    @Autowired
    private ImageProductMapper imageProductMapper;
    @Autowired
    private CartItemMapper cartItemMapper;

    @Override
    public CartResponse createCart(CartRequest request) {
        try {
            UserEntity userEntity = userRepository.findById(request.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            CartEntity cartEntity = cartRepository.findCartEntityByUserEntity_Id(request.getUserId());

            if (cartEntity == null) {
                cartEntity = new CartEntity();
                cartEntity.setUserEntity(userEntity);
                cartEntity.setCreatedAt(request.getCreatedAt());
                cartEntity.setCartItemEntityList(new ArrayList<>());
            }

            cartEntity.setUpdatedAt(request.getUpdatedAt());

            if (request.getCartItems() != null && !request.getCartItems().isEmpty()) {
                for (CartItemRequest itemReq : request.getCartItems()) {

                    Long productId = itemReq.getProductId();
                    if (productId == null) throw new RuntimeException("productId is required");

                    ProductEntity product = productRepository.findProductEntityByProductId(productId);
                    if (product == null) throw new RuntimeException("Product not found: " + productId);

                    // ✅ selected ids từ FE
                    List<Long> selectedIds = itemReq.getSelectedValueIds();

                    // ✅ product có biến thể không?
                    boolean hasVariant =
                            product.getProductVarriantList() != null && !product.getProductVarriantList().isEmpty();

                    // ✅ nếu có biến thể -> bắt buộc chọn
                    if (hasVariant && (selectedIds == null || selectedIds.isEmpty())) {
                        throw new RuntimeException("Please select variants for productId=" + productId);
                    }

                    // ✅ normalize key để merge
                    String selectedKey = normalizeSelectedValueIds(selectedIds);

                    // ✅ build text (hàm của bạn đã validate id hợp lệ)
                    String variantText = hasVariant ? buildVariantText(product, selectedIds) : "";

                    // ✅ OPTIONAL: dọn item cũ bị "[]" (do logic cũ) để khỏi sinh cartItem rác
                    if (hasVariant) {
                        cartEntity.getCartItemEntityList().removeIf(i ->
                                i.getProduct() != null
                                        && i.getProduct().getProductId().equals(productId)
                                        && (i.getSelectedValueIds() == null || "[]".equals(i.getSelectedValueIds()))
                        );
                    }

                    // ✅ merge theo (productId + selectedKey)
                    CartItemEntity existing = cartEntity.getCartItemEntityList().stream()
                            .filter(i -> i.getProduct() != null
                                    && i.getProduct().getProductId().equals(productId)
                                    && Objects.equals(i.getSelectedValueIds(), selectedKey))
                            .findFirst()
                            .orElse(null);

                    int qtyAdd = itemReq.getCartItemQuantity() == null ? 0 : itemReq.getCartItemQuantity();
                    if (qtyAdd <= 0) throw new RuntimeException("cartItemQuantity must be > 0");

                    double price = itemReq.getCartItemPrice() == null ? 0.0 : itemReq.getCartItemPrice();
                    if (price < 0) throw new RuntimeException("cartItemPrice must be >= 0");

                    if (existing != null) {
                        // ✅ tăng số lượng
                        existing.setCartItemQuantity(existing.getCartItemQuantity() + qtyAdd);

                        // ✅ update price mới nhất (nếu bạn muốn cố định giá lúc add thì bỏ dòng này)
                        existing.setCartItemPrice(price);

                        // ✅ total luôn tính theo qty * price
                        existing.setCartItemTotalPrice(existing.getCartItemQuantity() * price);

                        // snapshot option
                        existing.setSelectedValueIds(selectedKey);
                        existing.setVariantText(variantText);

                    } else {
                        CartItemEntity cartItemEntity = new CartItemEntity();
                        cartItemEntity.setCart(cartEntity);
                        cartItemEntity.setProduct(product);

                        cartItemEntity.setCartItemQuantity(qtyAdd);
                        cartItemEntity.setCartItemPrice(price);
                        cartItemEntity.setCartItemTotalPrice(qtyAdd * price);

                        cartItemEntity.setSelectedValueIds(selectedKey);
                        cartItemEntity.setVariantText(variantText);

                        cartEntity.getCartItemEntityList().add(cartItemEntity);
                    }
                }
            }

            // ✅ QUAN TRỌNG: tính lại cart total từ items, không lấy request.getCartTotalPrice()
            double total = cartEntity.getCartItemEntityList().stream()
                    .mapToDouble(i -> i.getCartItemTotalPrice() == null ? 0.0 : i.getCartItemTotalPrice())
                    .sum();

            cartEntity.setCartTotalPrice(total);

            CartEntity savedCart = cartRepository.saveAndFlush(cartEntity);
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

//            VoucherEntity voucherEntity = request.getVoucherId() != null
//                    ? voucherRepository.findVoucherEntitiesByVoucherId(request.getVoucherId())
//                    : null;

            CartEntity cartEntity = new CartEntity();
            cartEntity.setCartId(cartId);
            cartEntity.setUserEntity(userEntity);
            cartEntity.setCreatedAt(request.getCreatedAt());
            cartEntity.setCartTotalPrice(request.getCartTotalPrice());
//            cartEntity.setCartShippingFee(request.getCartShippingFee());
//            cartEntity.setCartTotalCoinEarned(request.getCartTotalCoinEarned());
//            cartEntity.setVoucher(voucherEntity);

            if (request.getCartItems() != null && !request.getCartItems().isEmpty()) {
                for (CartItemRequest itemReq : request.getCartItems()) {
                    ProductEntity pr = productRepository.findProductEntityByProductId(itemReq.getProductId());
                    CartItemEntity cartItemEntity = new CartItemEntity();
                    cartItemEntity.setCart(cartEntity);
                    if (itemReq.getCartItemId() != null ) {
                        cartItemEntity.setCartItemId(itemReq.getCartItemId());
                    }
                    cartItemEntity.setCartItemPrice(itemReq.getCartItemPrice());
//                    cartItemEntity.setCartItemOriginalPrice(itemReq.getCartItemOriginalPrice());
                    cartItemEntity.setCartItemQuantity(itemReq.getCartItemQuantity());
                    cartItemEntity.setCartItemTotalPrice(itemReq.getCartItemTotalPrice());
//                    cartItemEntity.setFlashSale(itemReq.isFlashSale());
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
    public CartItemResponse updateCartItem(Long cartId, Long cartItemId, CartItemRequest request) {
        CartEntity cartEntity = cartRepository.findCartEntityByCartId(cartId);
        ProductEntity productEntity = productRepository.findProductEntityByProductId(request.getProductId());
        CartItemEntity cartItemEntity = cartItemRepository.findByCartAndProductAndCartItemId(
                cartEntity, productEntity, cartItemId
        );
        if (cartItemEntity != null) {
            cartItemEntity.setCartItemQuantity(request.getCartItemQuantity());
            cartItemEntity.setCartItemPrice(request.getCartItemPrice());
            cartItemEntity.setCartItemTotalPrice(request.getCartItemPrice() * request.getCartItemQuantity());
            cartItemEntity.setProduct(productEntity); // nếu cho phép đổi sản phẩm
            cartItemEntity.setCart(cartEntity); // nhớ gán lại cart để tránh mất liên kết
            return cartItemMapper.toCartItemResponse(cartItemRepository.save(cartItemEntity));
        }
        return null;
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

    @Override
    public List<ProductVarriantResponse> getCartProductsVariantsByProductId(Long productId) {
        ProductEntity productEntity = productRepository.findProductEntityByProductId(productId);
        if (productEntity != null) {
            List<ProductVarriantEntity> productVarriantEntities = productVarriantRepository.findProductVarriantEntitiesByProductVarriants(productEntity);
            return productVarriantMapper.toResponseList(productVarriantEntities);
        }
        return null;

    }

    @Override
    public List<ImageProductResponse> getImageProduct(Long productId) {
        ProductEntity productEntity = productRepository.findProductEntityByProductId(productId);
        if (productEntity != null) {
            List<ImageProductEntity> image = imageProductRepository.findByImageProduct(productEntity);
            return imageProductMapper.toResponseList(image); // ✅ trùng kiểu
        }
        return null;
    }

    private String normalizeSelectedValueIds(List<Long> ids) {
        if (ids == null || ids.isEmpty()) return "[]";
        return ids.stream()
                .filter(Objects::nonNull)
                .distinct()
                .sorted()
                .map(String::valueOf)
                .collect(java.util.stream.Collectors.joining(",", "[", "]"));
    }

    private String buildVariantText(ProductEntity product, List<Long> selectedIds) {
        if (selectedIds == null || selectedIds.isEmpty()) return "";

        // lấy các option hợp lệ của product
        List<ProductVarriantEntity> allowed =
                productVarriantRepository.findProductVarriantEntitiesByProductVarriants(product);

        // chỉ lấy value đang active
        Map<Long, String> valueIdToText = allowed.stream()
                .filter(pv -> pv.getVarriantValue() != null && Boolean.TRUE.equals(pv.getVarriantValue().getIsActive()))
                .collect(java.util.stream.Collectors.toMap(
                        pv -> pv.getVarriantValue().getVarriantValueId(),
                        pv -> pv.getVarriants().getVarriantName() + ": " + pv.getVarriantValue().getValueName(),
                        (a, b) -> a
                ));

        // validate: mỗi selectedId phải thuộc product và active
        for (Long id : selectedIds) {
            if (id == null || !valueIdToText.containsKey(id)) {
                throw new RuntimeException("Invalid selectedValueId=" + id + " for productId=" + product.getProductId());
            }
        }

        // build text theo thứ tự ổn định
        return selectedIds.stream()
                .filter(Objects::nonNull)
                .distinct()
                .sorted()
                .map(valueIdToText::get)
                .collect(java.util.stream.Collectors.joining(", "));
    }

}