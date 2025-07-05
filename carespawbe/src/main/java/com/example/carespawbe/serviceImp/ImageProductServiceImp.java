package com.example.carespawbe.serviceImp;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.example.carespawbe.entity.shop.ImageProductEntity;
import com.example.carespawbe.entity.shop.ProductEntity;
import com.example.carespawbe.repository.shop.ImageProductRepository;
import com.example.carespawbe.repository.shop.ProductRepository;
import com.example.carespawbe.service.ImageProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Map;

@Service
public class ImageProductServiceImp implements ImageProductService {

    @Autowired
    private Cloudinary cloudinary;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ImageProductRepository imageProductRepository;

    @Override
    public ImageProductEntity uploadProductImage(MultipartFile file, Long productId) throws IOException {
        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
        String url = uploadResult.get("secure_url").toString();

        ImageProductEntity image = ImageProductEntity.builder()
                .imageProductUrl(url)
                .uploadedAt(LocalDateTime.now())
                .imageProduct(product)
                .build();

        return imageProductRepository.save(image);
    }
}

