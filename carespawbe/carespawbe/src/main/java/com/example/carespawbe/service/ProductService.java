package com.example.carespawbe.service;

import com.example.carespawbe.dto.request.ProductRequest;
import com.example.carespawbe.entity.ProductEntity;

public interface ProductService {
    ProductEntity createProduct(ProductRequest dto);
}
