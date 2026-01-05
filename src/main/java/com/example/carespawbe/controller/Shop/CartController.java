package com.example.carespawbe.controller.Shop;

import com.example.carespawbe.dto.Shop.request.CartItemRequest;
import com.example.carespawbe.dto.Shop.response.CartResponse;
import com.example.carespawbe.service.Shop.CartService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    // =========================
    // ADD TO CART (SKU + QTY)
    // Route giữ nguyên: POST /cart/add
    // Body: { productSkuId, cartItemQuantity }
    // userId lấy từ filter (request attribute)
    // =========================
    @PostMapping("/add")
    public ResponseEntity<CartResponse> addToCart(
            HttpServletRequest request,
            @RequestBody CartItemRequest body
    ) {
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) throw new RuntimeException("User not authenticated");

        return ResponseEntity.ok(cartService.addItem(userId, body));
    }

    // =========================
    // UPDATE ITEM (by cartItemId)
    // Route giữ nguyên: PUT /cart/update?cartId=...
    // => cartId param giữ nguyên để không đổi FE, nhưng logic update theo cartItemId
    // Body: { cartItemId, cartItemQuantity } (productSkuId không cần nếu không đổi SKU)
    // =========================
    @PutMapping("/update")
    public ResponseEntity<CartResponse> updateCartItemFast(
            HttpServletRequest request,
            @RequestParam("cartId") Long cartId,   // giữ nguyên route param
            @RequestBody CartItemRequest body
    ) {
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) throw new RuntimeException("User not authenticated");

        if (body.getCartItemId() == null) {
            throw new RuntimeException("cartItemId is required for update");
        }

        // (optional) bạn có thể validate cartId thuộc userId trong service
        return ResponseEntity.ok(cartService.updateItem(userId, body.getCartItemId(), body));
    }

    // =========================
    // DELETE CART (giữ nguyên route)
    // Route: DELETE /cart/delete?cartId=...
    // Bạn muốn "xóa hẳn cart" -> deleteCart
    // Nếu muốn "clear items" -> clearCart
    // =========================
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteCart(@RequestParam("cartId") Long cartId) {
        cartService.deleteCart(cartId);
        return ResponseEntity.ok("Xoá thành công.");
    }

    // (Nếu bạn muốn clear cart thay vì delete cart, dùng endpoint này tùy bạn giữ hay không)
    // @DeleteMapping("/clear")
    // public ResponseEntity<String> clearCart(@RequestParam("cartId") Long cartId) {
    //     cartService.clearCart(cartId);
    //     return ResponseEntity.ok("Đã clear cart items.");
    // }

    // =========================
    // GET CART BY USER (giữ nguyên route)
    // Route: GET /cart/get/{userId}
    // =========================
    @GetMapping("/get/{userId}")
    public ResponseEntity<CartResponse> getCartByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(cartService.getCartByUserId(userId));
    }

    // =========================
    // UPDATE ITEM (giữ nguyên route cũ)
    // Route: PUT /cart/{cartId}/item/{cartItemId}
    // Trả về CartResponse để đồng bộ flow mới
    // =========================
    @PutMapping("/{cartId}/item/{cartItemId}")
    public ResponseEntity<CartResponse> updateCartItem(
            HttpServletRequest request,
            @PathVariable Long cartId,
            @PathVariable Long cartItemId,
            @RequestBody CartItemRequest body
    ) {
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) throw new RuntimeException("User not authenticated");

        // đảm bảo body có quantity
        body.setCartItemId(cartItemId);
        body.setCartId(cartId);

        return ResponseEntity.ok(cartService.updateItem(userId, cartItemId, body));
    }

    // =========================
    // REMOVE ITEM (thêm endpoint nhưng KHÔNG đổi route cũ nếu bạn không muốn)
    // Nếu FE đã có remove-item riêng thì dùng cái này
    // =========================
    @DeleteMapping("/{cartId}/item/{cartItemId}")
    public ResponseEntity<CartResponse> removeItem(
            HttpServletRequest request,
            @PathVariable Long cartId,
            @PathVariable Long cartItemId
    ) {
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) throw new RuntimeException("User not authenticated");

        return ResponseEntity.ok(cartService.removeItem(userId, cartItemId));
    }
}
