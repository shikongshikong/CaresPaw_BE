package com.example.carespawbe.controller;

import com.example.carespawbe.entity.shop.ImageProductEntity;
import com.example.carespawbe.service.ImageProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/product-images")
public class ImageProductController {

    @Autowired
    private ImageProductService imageProductService;

    @PostMapping("/upload")
    public ResponseEntity<?> uploadImage(@RequestParam("file") MultipartFile file,
                                         @RequestParam("productId") Long productId) {
        try {
            ImageProductEntity image = imageProductService.uploadProductImage(file, productId);
            return ResponseEntity.ok(image);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Upload ảnh thất bại: " + e.getMessage());
        }
    }
}
