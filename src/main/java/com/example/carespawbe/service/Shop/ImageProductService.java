package com.example.carespawbe.service.Shop;

import com.example.carespawbe.entity.Shop.ImageProductEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageProductService {
    ImageProductEntity uploadProductImage(MultipartFile file, Long productId) throws IOException;
}

