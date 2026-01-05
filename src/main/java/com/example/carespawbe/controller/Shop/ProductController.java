package com.example.carespawbe.controller.Shop;

import com.example.carespawbe.dto.Shop.request.ProductRequest;
import com.example.carespawbe.dto.Shop.response.ProductDetailResponse;
import com.example.carespawbe.dto.Shop.response.ProductResponse;
import com.example.carespawbe.service.Shop.ProductService;
import jakarta.servlet.annotation.MultipartConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@MultipartConfig
@CrossOrigin(origins = "*")
public class ProductController {

    private final ProductService productService;

    @PostMapping(value = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createProduct(
            @RequestParam("productName") String productName,
            @RequestParam("productPrice") Double productPrice,

            // ✅ OPTIONAL: FE có thể không gửi
            @RequestParam(value = "productAmount", required = false) Integer productAmount,

            @RequestParam("productStatus") Integer productStatus,
            @RequestParam("productUsing") String productUsing,
            @RequestParam("categoryId") Long categoryId,

            @RequestParam(value = "images", required = false) MultipartFile[] images,
            @RequestParam(value = "video", required = false) MultipartFile video,

            @RequestHeader("Authorization") String authorizationHeader
    ) {
        try {
            ProductRequest request = new ProductRequest();
            request.setProductName(productName);
            request.setProductPrice(productPrice);

            // ✅ default 0 để DB không null
            request.setProductAmount(productAmount == null ? 0 : productAmount);

            request.setProductStatus(productStatus);
            request.setProductUsing(productUsing);
            request.setCategoryId(categoryId);

            ProductResponse response = productService.createProduct(request, images, video, authorizationHeader);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping(value = "/{productId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> updateProduct(
            @PathVariable Long productId,
            @RequestParam("productName") String productName,
            @RequestParam("productPrice") Double productPrice,

            // ✅ OPTIONAL
            @RequestParam(value = "productAmount", required = false) Integer productAmount,

            @RequestParam("productStatus") Integer productStatus,
            @RequestParam("productUsing") String productUsing,
            @RequestParam("categoryId") Long categoryId,

            @RequestParam(value = "images", required = false) MultipartFile[] images,
            @RequestParam(value = "video", required = false) MultipartFile video,

            @RequestHeader("Authorization") String authorizationHeader
    ) {
        try {
            ProductRequest request = new ProductRequest();
            request.setProductName(productName);
            request.setProductPrice(productPrice);

            // ✅ default 0 nếu FE không gửi
            request.setProductAmount(productAmount == null ? 0 : productAmount);

            request.setProductStatus(productStatus);
            request.setProductUsing(productUsing);
            request.setCategoryId(categoryId);

            ProductResponse response = productService.updateProduct(productId, request, images, video, authorizationHeader);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/{productId}")
    public ResponseEntity<?> getProductDetailById(@PathVariable Long productId) {
        try {
            ProductDetailResponse response = productService.getProductDetailById(productId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @DeleteMapping("/{productId}")
    public ResponseEntity<?> deleteProduct(
            @PathVariable Long productId,
            @RequestHeader("Authorization") String authorizationHeader
    ) {
        try {
            productService.deleteProduct(productId, authorizationHeader);
            return ResponseEntity.ok(Map.of("message", "Product deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/getAllProduct")
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        try {
            List<ProductResponse> products = productService.getAllProducts();
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/new")
    public ResponseEntity<List<ProductResponse>> getNewProducts() {
        List<ProductResponse> newProducts = productService.getNewProducts();
        return ResponseEntity.ok(newProducts);
    }

    @GetMapping("/shop/{shopId}")
    public ResponseEntity<?> getAllProductsByShopId(@PathVariable Long shopId) {
        try {
            List<ProductResponse> products = productService.getAllProductsByShopId(shopId);
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/shop/{shopId}/category/{categoryId}")
    public ResponseEntity<List<ProductResponse>> getProductsByShopAndCategory(
            @PathVariable Long shopId,
            @PathVariable Long categoryId
    ) {
        List<ProductResponse> result = productService.getAllProductsByShopIdAndCategoryId(shopId, categoryId);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/category/{categoryId}")
    public List<ProductResponse> getProductsByCategory(@PathVariable Long categoryId) {
        return productService.getProductsByCategory(categoryId);
    }

    // =========================
    // ✅ ADD: Best Sellers
    // /products/best-sellers?limit=10
    // =========================
    @GetMapping("/best-sellers")
    public List<ProductResponse> getBestSellers() {
        return productService.getBestSellers();
    }

}
