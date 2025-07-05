package com.example.carespawbe.service;

import com.example.carespawbe.entity.shop.ImageProductEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageProductService {
    ImageProductEntity uploadProductImage(MultipartFile file, Long productId) throws IOException;
}

