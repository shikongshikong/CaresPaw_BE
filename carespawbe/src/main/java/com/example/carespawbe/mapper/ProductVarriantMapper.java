package com.example.carespawbe.mapper;

import com.example.carespawbe.dto.response.ProductVarriantResponse;
import com.example.carespawbe.entity.shop.ProductVarriantEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductVarriantMapper {

    @Mapping(target = "varriantId", source = "varriants.varriantId")
    @Mapping(target = "productVarriantValue", source = "productVarriantValue")
    ProductVarriantResponse toResponse(ProductVarriantEntity entity);

    List<ProductVarriantResponse> toResponseList(List<ProductVarriantEntity> entities);
}
