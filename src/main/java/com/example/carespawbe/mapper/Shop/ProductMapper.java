package com.example.carespawbe.mapper.Shop;

import com.example.carespawbe.dto.Shop.request.ProductRequest;
import com.example.carespawbe.dto.Shop.response.ProductResponse;
import com.example.carespawbe.dto.Shop.response.SkuResponse;
import com.example.carespawbe.entity.Shop.ImageProductEntity;
import com.example.carespawbe.entity.Shop.ProductEntity;
import com.example.carespawbe.entity.Shop.ProductSkuEntity;
import org.mapstruct.*;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(
        componentModel = "spring",
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
    @Mapping(target = "shopLogo", source = "shop.shopLogo")
    @Mapping(target = "imageUrls", expression = "java(mapImages(productEntity.getImageProductList()))")

    // ✅ QUAN TRỌNG: map sold của product (tổng sold)
    @Mapping(target = "sold", expression = "java(productEntity.getSold() == null ? 0L : productEntity.getSold())")

    // ✅ SKU list optional
    @Mapping(target = "skus", expression = "java(mapSkus(productEntity.getSkuList()))")
    ProductResponse toProductResponse(ProductEntity productEntity);

    List<ProductResponse> toProductResponseList(List<ProductEntity> productEntityList);

    default List<String> mapImages(List<ImageProductEntity> images) {
        if (images == null) return null;
        return images.stream()
                .map(ImageProductEntity::getImageProductUrl)
                .collect(Collectors.toList());
    }

    // ✅ map sku list (giữ nhẹ, không cần join sâu)
    default List<SkuResponse> mapSkus(List<ProductSkuEntity> skus) {
        if (skus == null) return null;
        return skus.stream()
                .map(s -> SkuResponse.builder()
                        .productSkuId(s.getProductSkuId())
                        .skuCode(s.getSkuCode())
                        .skuName(s.getSkuName())
                        .stock(s.getStock())
                        .price(s.getPrice())

                        // ✅ QUAN TRỌNG: map sold của sku (NULL -> 0)
                        .sold(s.getSold() == null ? 0L : s.getSold())

                        .isActive(s.getIsActive())
                        .build())
                .collect(Collectors.toList());
    }
}
