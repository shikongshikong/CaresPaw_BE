package com.example.carespawbe.mapper.Shop;

import com.example.carespawbe.dto.Shop.request.ProductRequest;
import com.example.carespawbe.dto.Shop.response.ProductResponse;
import com.example.carespawbe.entity.Shop.ImageProductEntity;
import com.example.carespawbe.entity.Shop.ProductEntity;
import org.mapstruct.*;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(
        componentModel = "spring",
        uses = { ProductVarriantMapper.class },
        nullValueIterableMappingStrategy = NullValueMappingStrategy.RETURN_NULL
)
public interface ProductMapper {

    @Mapping(target = "categoryId", source = "category.categoryId")
    @Mapping(target = "shopId", source = "shop.shopId")
    ProductRequest toProductRequest(ProductEntity productEntity);

    @Mapping(target = "categoryId", source = "category.categoryId")
    @Mapping(target = "categoryName", source = "category.categoryName")

    @Mapping(target = "shopId", source = "shop.shopId")
    @Mapping(target = "shopName", source = "shop.shopName")
    @Mapping(target = "imageUrls", expression = "java(mapImages(productEntity.getImageProductList()))")
    // ✅ ĐỔI: map trực tiếp list entity -> list response, MapStruct sẽ gọi ProductVarriantMapper
    @Mapping(target = "productVarriants", source = "productVarriantList")

    @Mapping(target = "sold", source = "sold")
    @Mapping(target = "rating", source = "rating")
    ProductResponse toProductResponse(ProductEntity productEntity);

    @Mapping(target = "imageUrls", expression = "java(mapImages(productEntity.getImageProductList()))")
    @Mapping(target = "productVarriants", source = "productVarriantList")
    List<ProductResponse> toProductResponseList(List<ProductEntity> productEntityList);

    // map list ảnh
    default List<String> mapImages(List<ImageProductEntity> images) {
        if (images == null) return null;
        return images.stream()
                .map(ImageProductEntity::getImageProductUrl)
                .collect(Collectors.toList());
    }
}
