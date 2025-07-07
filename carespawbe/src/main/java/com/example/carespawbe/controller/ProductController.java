package com.example.carespawbe.controller;

import com.example.carespawbe.dto.request.ProductRequest;
import com.example.carespawbe.dto.request.ProductVarriantRequest;
import com.example.carespawbe.dto.response.ProductResponse;
import com.example.carespawbe.service.ProductService;
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
@RequestMapping("/api/products")
@RequiredArgsConstructor
@MultipartConfig
@CrossOrigin(origins = "*")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping(value = "/add", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createProduct(
            @RequestParam("productName") String productName,
            @RequestParam("productDescribe") String productDescribe,
            @RequestParam("productPrice") Double productPrice,
            @RequestParam("productAmount") Integer productAmount,
            @RequestParam("productStatus") Integer productStatus,
            @RequestParam("productUsing") String productUsing,
            @RequestParam("categoryId") Long categoryId,
            @RequestParam("shopId") Long shopId,
            @RequestParam(value = "productVarriants", required = false) String productVarriantsJson,
            @RequestParam(value = "images", required = false) MultipartFile[] images,
            @RequestPart(value = "video", required = false) MultipartFile video
    ) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            List<ProductVarriantRequest> productVarriants = null;

            if (productVarriantsJson != null && !productVarriantsJson.isEmpty()) {
                // Parse JSON string thành List<ProductVarriantRequest>
                productVarriants = objectMapper.readValue(
                        productVarriantsJson,
                        new TypeReference<List<ProductVarriantRequest>>() {}
                );
            }

            ProductRequest request = new ProductRequest();
            request.setProductName(productName);
            request.setProductDescribe(productDescribe);
            request.setProductPrice(productPrice);
            request.setProductAmount(productAmount);
            request.setProductStatus(productStatus);
            request.setProductUsing(productUsing);
            request.setCategoryId(categoryId);
            request.setShopId(shopId);
            request.setProductVarriants(productVarriants);

            ProductResponse response = productService.createProduct(request, images, video);
            System.out.println("Received request: " + request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @PutMapping("/{productId}")
    public ResponseEntity<?> updateProduct(
            @PathVariable Long productId,
            @RequestParam("productName") String productName,
            @RequestParam("productDescribe") String productDescribe,
            @RequestParam("productPrice") Double productPrice,
            @RequestParam("productAmount") Integer productAmount,
            @RequestParam("productStatus") Integer productStatus,
            @RequestParam("productUsing") String productUsing,
            @RequestParam("categoryId") Long categoryId,
            @RequestParam("shopId") Long shopId,
            @RequestParam(value = "productVarriants", required = false) String productVarriantsJson,
            @RequestParam(value = "images", required = false) MultipartFile[] images,
            @RequestPart(value = "video", required = false) MultipartFile video
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
            request.setProductDescribe(productDescribe);
            request.setProductPrice(productPrice);
            request.setProductAmount(productAmount);
            request.setProductStatus(productStatus);
            request.setProductUsing(productUsing);
            request.setCategoryId(categoryId);
            request.setShopId(shopId);
            request.setProductVarriants(productVarriants);

            ProductResponse response = productService.updateProduct(productId, request, images, video);
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
    public ResponseEntity<?> deleteProduct(@PathVariable Long productId) {
        try {
            productService.deleteProduct(productId);
            return ResponseEntity.ok(Map.of("message", "Product deleted successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

}
