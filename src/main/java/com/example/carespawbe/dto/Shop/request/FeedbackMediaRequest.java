package com.example.carespawbe.dto.Shop.request;

import org.springframework.web.multipart.MultipartFile;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FeedbackMediaRequest {
    private String resourceType;
    private MultipartFile file;
}
