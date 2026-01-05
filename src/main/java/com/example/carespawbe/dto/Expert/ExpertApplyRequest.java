package com.example.carespawbe.dto.Expert;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Setter
@Getter
public class ExpertApplyRequest {
    private String fullName;
    private String biography;
    private Integer experienceYear;
    private Double price;
    private MultipartFile idImage;
    private MultipartFile certificateImage;
    private String location;
    private Long userId;
    private String portfolioLink;
    private List<Long> expertCategoryIds;
}
