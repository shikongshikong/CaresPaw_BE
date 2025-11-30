package com.example.carespawbe.mapper;

import com.example.carespawbe.dto.response.ImageProductResponse;
import com.example.carespawbe.dto.response.ProductVarriantResponse;
import com.example.carespawbe.entity.shop.ImageProductEntity;
import com.example.carespawbe.entity.shop.ProductVarriantEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ImageProductMapper {
    @Mapping(target = "imageProductUrl", source = "imageProductUrl")
    @Mapping(target = "imagePublicId", source = "imagePublicId")
    ImageProductResponse toResponse(ImageProductEntity entities);

    @Mapping(target = "imageProductUrl", source = "imageProductUrl")
    @Mapping(target = "imagePublicId", source = "imagePublicId")
    List<ImageProductResponse> toResponseList(List<ImageProductEntity> entities);
}
