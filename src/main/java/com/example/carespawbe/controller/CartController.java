package com.example.carespawbe.controller;

import com.example.carespawbe.dto.request.CartRequest;
import com.example.carespawbe.dto.request.CategoryRequest;
import com.example.carespawbe.dto.response.CartResponse;
import com.example.carespawbe.dto.response.CategoryResponse;
import com.example.carespawbe.service.CartService;
import com.example.carespawbe.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping("/add")
    public ResponseEntity<CartResponse> createCart(
            @RequestBody CartRequest cartRequest
    ) {
        try {
            return ResponseEntity.ok(cartService.createCart(cartRequest));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PutMapping("/add")
    public ResponseEntity<CartResponse> updateCategory(
            @RequestParam("cartId") Long cartId,
            @RequestBody CartRequest cartRequest
    ) {
        try {
            return ResponseEntity.ok(cartService.updateCart(cartId,cartRequest));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

//    @DeleteMapping("/delete/{categoryId}")
//    public ResponseEntity<?> deleteCategory(@PathVariable Long categoryId) {
//        categoryService.deleteCategory(categoryId);
//        return ResponseEntity.ok("Xoá danh mục với ID " + categoryId + " thành công.");
//    }
//
//    @GetMapping("/getAll")
//    public ResponseEntity<List<CategoryResponse>> getAllCategories() {
//        return ResponseEntity.ok(categoryService.getAllCategories());
//    }
//
//    @GetMapping("/get/{categoryId}")
//    public ResponseEntity<CategoryResponse> getCategoryById(@PathVariable Long categoryId) {
//        return ResponseEntity.ok(categoryService.getCategoryById(categoryId));
//    }
}
