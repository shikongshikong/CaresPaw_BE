package com.example.carespawbe.mapper.Shop;

import com.example.carespawbe.dto.Shop.request.ProductRequest;
import com.example.carespawbe.dto.Shop.response.ProductResponse;
import com.example.carespawbe.dto.Shop.response.ProductVarriantResponse;
import com.example.carespawbe.entity.Shop.ImageProductEntity;
import com.example.carespawbe.entity.Shop.ProductEntity;
import com.example.carespawbe.entity.Shop.ProductVarriantEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueMappingStrategy;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", nullValueIterableMappingStrategy = NullValueMappingStrategy.RETURN_NULL)
public interface ProductMapper {

    @Mapping(target = "categoryId", source = "category.categoryId")
    @Mapping(target = "shopId", source = "shop.shopId")
    ProductRequest toProductRequest(ProductEntity productEntity);

    @Mapping(target = "categoryId", source = "category.categoryId")
    @Mapping(target = "shopId", source = "shop.shopId")
    @Mapping(target = "shopName", source = "shop.shopName")
    @Mapping(target = "imageUrls", expression = "java(mapImages(productEntity.getImageProductList()))")
    @Mapping(target = "productVarriants", expression = "java(mapVarriants(productEntity.getProductVarriantList()))")
    ProductResponse toProductResponse(ProductEntity productEntity);

    @Mapping(target = "productVarriants", expression = "java(mapVarriants(productEntity.getProductVarriantList()))")
    List<ProductResponse> toProductResponseList(List<ProductEntity> productEntityList);

    // Hàm thủ công map list ảnh
    default List<String> mapImages(List<ImageProductEntity> images) {
        if (images == null) return null;
        return images.stream()
                .map(ImageProductEntity::getImageProductUrl)
                .collect(Collectors.toList());
    }

    // Hàm thủ công map list biến thể sản phẩm
    default List<ProductVarriantResponse> mapVarriants(List<ProductVarriantEntity> variants) {
        if (variants == null) return null;
        return variants.stream().map(entity -> {
            ProductVarriantResponse response = new ProductVarriantResponse();
            if (entity.getVarriants() != null) {
                response.setVarriantId(entity.getVarriants().getVarriantId());
            }
            response.setProductVarriantValue(entity.getProductVarriantValue());
            return response;
        }).collect(Collectors.toList());
    }
}
