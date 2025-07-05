package com.example.carespawbe.mapper;

import com.example.carespawbe.dto.request.ProductRequest;
import com.example.carespawbe.dto.response.ProductResponse;
import com.example.carespawbe.entity.shop.ProductEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring", nullValueIterableMappingStrategy = NullValueMappingStrategy.RETURN_NULL)
public interface ProductMapper {
    @Mapping(target = "categoryId", source = "category.categoryId")
    @Mapping(target = "shopId", source = "shop.shopId")
//    @Mapping(target = "imageUrls", source = "imageProductList.imageProduct")
    @Mapping(target = "imageUrls", expression = "java(productEntity.getImageProductList() != null ? productEntity.getImageProductList().stream().map(img -> img.getImageProductUrl()).toList() : null)")
    ProductRequest toProductRequest(ProductEntity productEntity);

    @Mapping(target = "categoryId", source = "category.categoryId")
    @Mapping(target = "shopId", source = "shop.shopId")
    @Mapping(target = "imageUrls", expression = "java(productEntity.getImageProductList() != null ? productEntity.getImageProductList().stream().map(img -> img.getImageProductUrl()).toList() : null)")
    ProductResponse toProductResponse(ProductEntity productEntity);

    List<ProductResponse> toProductResponseList(List<ProductEntity> productEntityList);
}