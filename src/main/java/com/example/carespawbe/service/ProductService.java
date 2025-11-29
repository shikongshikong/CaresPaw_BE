package com.example.carespawbe.service;

import com.example.carespawbe.dto.request.ProductRequest;
import com.example.carespawbe.dto.response.ProductResponse;
import com.example.carespawbe.entity.shop.ProductEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {
    ProductResponse createProduct(ProductRequest dto, MultipartFile[] image, MultipartFile video, String authorizationHeader);
    ProductResponse updateProduct(Long productId, ProductRequest request, MultipartFile[] images, MultipartFile video, String authorizationHeader);
    ProductResponse getProductById(Long productId);
    void deleteProduct(Long productId, String authorizationHeader);
    List<ProductResponse> getAllProducts();
    List<ProductResponse> getNewProducts();

}
