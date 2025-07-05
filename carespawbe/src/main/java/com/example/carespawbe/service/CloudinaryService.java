package com.example.carespawbe.service;

import org.springframework.web.multipart.MultipartFile;



public interface CloudinaryService {
    String uploadImage(MultipartFile file);
    String uploadVideo(MultipartFile file);
}
