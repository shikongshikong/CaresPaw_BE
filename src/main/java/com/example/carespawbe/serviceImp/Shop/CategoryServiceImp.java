package com.example.carespawbe.serviceImp.Shop;

import com.example.carespawbe.dto.Shop.request.CategoryRequest;
import com.example.carespawbe.dto.Shop.response.CategoryResponse;
import com.example.carespawbe.entity.Shop.CategoryEntity;
import com.example.carespawbe.mapper.Shop.CategoryMapper;
import com.example.carespawbe.repository.Shop.CategoryRepository;
import com.example.carespawbe.service.Shop.CategoryService;
import com.example.carespawbe.service.CloudinaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CategoryServiceImp implements CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private CloudinaryService cloudinaryService;
    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public CategoryResponse createCategory(CategoryRequest request, MultipartFile categoryLogo) {
        try {
            CategoryEntity categoryEntity = new CategoryEntity();
            categoryEntity.setCategoryName(request.getCategoryName());

            if (categoryLogo != null && !categoryLogo.isEmpty()) {
                Map<String, String> result = cloudinaryService.uploadImageUrlAndPublicId(categoryLogo, "categories");
                categoryEntity.setCategoryLogo(result.get("url"));
                categoryEntity.setCategoryLogoPublicId(result.get("public_id"));
            } else {
                categoryEntity.setCategoryLogo("");
                categoryEntity.setCategoryLogoPublicId(""); // hoặc null nếu database chấp nhận
            }
            return categoryMapper.toCategoryResponse(categoryRepository.save(categoryEntity));
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    @Override
    public CategoryResponse updateCategory(Long id, CategoryRequest request, MultipartFile categoryLogo) {
        try {
            CategoryEntity categoryEntity = categoryRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Category not found with ID: " + id));

            categoryEntity.setCategoryName(request.getCategoryName());

            if (categoryLogo != null && !categoryLogo.isEmpty()) {
                Map<String, String> result = cloudinaryService.uploadImageUrlAndPublicId(categoryLogo, "categories");
                categoryEntity.setCategoryLogo(result.get("url"));
                categoryEntity.setCategoryLogoPublicId(result.get("public_id"));
            }

            CategoryEntity saved = categoryRepository.save(categoryEntity);
            return categoryMapper.toCategoryResponse(saved);

        } catch (Exception e) {
            e.printStackTrace(); // in log chi tiết
            throw new RuntimeException("Update category failed: " + e.getMessage());
        }
    }

    @Override
    public void deleteCategory(Long categoryId) {
        CategoryEntity category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy danh mục với id = " + categoryId));

        // Nếu có logo public_id thì xóa ảnh cũ khỏi Cloudinary
        if (category.getCategoryLogoPublicId() != null) {
            cloudinaryService.deleteImage(category.getCategoryLogoPublicId());
        }

        // Xóa category
        categoryRepository.delete(category);
    }

    @Override
    public List<CategoryResponse> getAllCategories() {
        List<CategoryEntity> categories = categoryRepository.findAll();
        return categories.stream()
                .map(categoryMapper::toCategoryResponse)
                .collect(Collectors.toList());
    }

    @Override
    public CategoryResponse getCategoryById(Long categoryId) {
        CategoryEntity category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + categoryId));
        return categoryMapper.toCategoryResponse(category);
    }

}
