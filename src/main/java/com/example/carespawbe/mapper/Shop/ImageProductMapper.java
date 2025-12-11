package com.example.carespawbe.mapper.Shop;

import com.example.carespawbe.dto.Shop.response.ImageProductResponse;
import com.example.carespawbe.entity.Shop.ImageProductEntity;
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
