package com.example.carespawbe.controller;

import com.example.carespawbe.dto.ProductRequestDTO;
import com.example.carespawbe.entity.ProductEntity;
import com.example.carespawbe.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @PostMapping("/create")
    public ResponseEntity<?> createProduct(@RequestBody ProductRequestDTO dto) {
        try {
            ProductEntity product = productService.createProduct(dto);
            return ResponseEntity.ok(product);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Tạo sản phẩm thất bại: " + e.getMessage());
        }
    }
}
