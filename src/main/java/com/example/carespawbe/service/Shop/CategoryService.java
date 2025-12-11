package com.example.carespawbe.service.Shop;

import com.example.carespawbe.dto.Shop.request.CategoryRequest;
import com.example.carespawbe.dto.Shop.response.CategoryResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CategoryService {
    CategoryResponse createCategory(CategoryRequest request, MultipartFile categoryLogo);
    CategoryResponse updateCategory(Long categoryId, CategoryRequest request, MultipartFile categoryLogo);
    void deleteCategory(Long categoryId);
    List<CategoryResponse> getAllCategories();
    CategoryResponse getCategoryById(Long categoryId);
}
