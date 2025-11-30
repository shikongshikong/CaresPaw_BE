package com.example.carespawbe.service;

import org.springframework.web.multipart.MultipartFile;

import java.util.Map;


public interface CloudinaryService {
    String uploadImage(MultipartFile file);
    String uploadVideo(MultipartFile file);
    void deleteImage(String publicId);
    void deleteVideo(String publicId);
    Map<String, String> uploadImageUrlAndPublicId(MultipartFile file, String folder);
    Map<String, String> uploadVideoUrlAndPublicId(MultipartFile file, String folder);
}
