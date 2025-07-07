package com.example.carespawbe.service;

import com.example.carespawbe.dto.request.ProductRequest;
import com.example.carespawbe.dto.response.ProductResponse;
import com.example.carespawbe.entity.shop.ProductEntity;
import org.springframework.web.multipart.MultipartFile;

public interface ProductService {
    ProductResponse createProduct(ProductRequest dto, MultipartFile[] image, MultipartFile video);
    ProductResponse updateProduct(Long productId, ProductRequest request, MultipartFile[] images, MultipartFile video);
    ProductResponse getProductById(Long productId);
    void deleteProduct(Long productId);
}
