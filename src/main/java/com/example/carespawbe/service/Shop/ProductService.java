package com.example.carespawbe.service.Shop;

import com.example.carespawbe.dto.Shop.request.ProductRequest;
import com.example.carespawbe.dto.Shop.response.ProductResponse;
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
