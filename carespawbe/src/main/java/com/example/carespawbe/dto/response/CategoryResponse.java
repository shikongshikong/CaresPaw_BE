package com.example.carespawbe.dto.response;


import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryResponse {
    private Long categoryId;
    private String categoryName;
    private String categoryLogo;
}
