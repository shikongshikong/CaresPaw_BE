package com.example.carespawbe.controller;

import com.example.carespawbe.dto.request.CategoryRequest;
import com.example.carespawbe.dto.response.CategoryResponse;
import com.example.carespawbe.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @PostMapping("/add")
    public ResponseEntity<CategoryResponse> createCategory(
            @RequestParam("categoryName") String categoryName,
            @RequestParam(value = "categoryLogo", required = false) MultipartFile categoryLogo
    ) {
        try {
            CategoryRequest categoryRequest = new CategoryRequest();
            categoryRequest.setCategoryName(categoryName);
            return ResponseEntity.ok(categoryService.createCategory(categoryRequest, categoryLogo));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @PutMapping("/update/{categoryId}")
    public ResponseEntity<CategoryResponse> updateCategory(
            @PathVariable Long categoryId,
            @RequestParam("categoryName") String categoryName,
            @RequestParam(value = "categoryLogo", required = false) MultipartFile categoryLogo
    ) {
        try {
            CategoryRequest request = new CategoryRequest();
            request.setCategoryName(categoryName);
            return ResponseEntity.ok(categoryService.updateCategory(categoryId, request, categoryLogo));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @DeleteMapping("/delete/{categoryId}")
    public ResponseEntity<?> deleteCategory(@PathVariable Long categoryId) {
        categoryService.deleteCategory(categoryId);
        return ResponseEntity.ok("Xoá danh mục với ID " + categoryId + " thành công.");
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<CategoryResponse>> getAllCategories() {
        return ResponseEntity.ok(categoryService.getAllCategories());
    }

    @GetMapping("/get/{categoryId}")
    public ResponseEntity<CategoryResponse> getCategoryById(@PathVariable Long categoryId) {
        return ResponseEntity.ok(categoryService.getCategoryById(categoryId));
    }
}
