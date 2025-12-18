package com.example.carespawbe.controller.Shop;

import com.example.carespawbe.dto.Shop.request.ProductRequest;
import com.example.carespawbe.dto.Shop.request.ProductVarriantRequest;
import com.example.carespawbe.dto.Shop.response.ProductResponse;
import com.example.carespawbe.service.Shop.ProductService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.MultipartConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private ProductService productService;

    @PostMapping(value = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createProduct(
            @RequestParam("productName") String productName,
            @RequestParam("productPrice") Double productPrice,
            @RequestParam("productAmount") Integer productAmount,
            @RequestParam("productStatus") Integer productStatus,
            @RequestParam("productUsing") String productUsing,
            @RequestParam("categoryId") Long categoryId,

            @RequestParam(value = "productVarriants", required = false) String productVarriantsJson,
            @RequestParam(value = "images", required = false) MultipartFile[] images,
            @RequestParam(value = "video", required = false) MultipartFile video,

            // THÊM Authorization header
            @RequestHeader("Authorization") String authorizationHeader
    ) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            List<ProductVarriantRequest> productVarriants = null;

            if (productVarriantsJson != null && !productVarriantsJson.isEmpty()) {
                productVarriants = objectMapper.readValue(
                        productVarriantsJson,
                        new TypeReference<List<ProductVarriantRequest>>() {}
                );
            }

            ProductRequest request = new ProductRequest();
            request.setProductName(productName);
            request.setProductPrice(productPrice);
            request.setProductAmount(productAmount);
            request.setProductStatus(productStatus);
            request.setProductUsing(productUsing);
            request.setCategoryId(categoryId);

            request.setProductVarriants(productVarriants);

            // Gọi service có token
            ProductResponse response = productService.createProduct(request, images, video, authorizationHeader);

            System.out.println("Received request: " + request);
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
//            @RequestParam("productDescribe") String productDescribe,
            @RequestParam("productPrice") Double productPrice,
//            @RequestParam("productPriceSale") Double productPriceSale,
            @RequestParam("productAmount") Integer productAmount,
            @RequestParam("productStatus") Integer productStatus,
            @RequestParam("productUsing") String productUsing,
            @RequestParam("categoryId") Long categoryId,

            @RequestParam(value = "productVarriants", required = false) String productVarriantsJson,
            @RequestParam(value = "images", required = false) MultipartFile[] images,
            @RequestParam(value = "video", required = false) MultipartFile video,

            // THÊM Authorization header
            @RequestHeader("Authorization") String authorizationHeader
    ) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            List<ProductVarriantRequest> productVarriants = null;

            if (productVarriantsJson != null && !productVarriantsJson.isEmpty()) {
                productVarriants = objectMapper.readValue(
                        productVarriantsJson,
                        new TypeReference<List<ProductVarriantRequest>>() {}
                );
            }

            ProductRequest request = new ProductRequest();
            request.setProductName(productName);
//            request.setProductDescribe(productDescribe);
            request.setProductPrice(productPrice);
//            request.setProductPriceSale(productPriceSale);
            request.setProductAmount(productAmount);
            request.setProductStatus(productStatus);
            request.setProductUsing(productUsing);
            request.setCategoryId(categoryId);

            request.setProductVarriants(productVarriants);

            // Gọi service có token
            ProductResponse response = productService.updateProduct(productId, request, images, video, authorizationHeader);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @GetMapping("/{productId}")
    public ResponseEntity<?> getProductById(@PathVariable Long productId) {
        try {
            ProductResponse response = productService.getProductById(productId);
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
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
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
}
