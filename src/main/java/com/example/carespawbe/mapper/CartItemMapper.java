package com.example.carespawbe.mapper;

import com.example.carespawbe.dto.request.CartItemRequest;
import com.example.carespawbe.dto.response.CartItemResponse;
import com.example.carespawbe.entity.shop.CartItemEntity;
import org.mapstruct.*;

import java.util.List;
@Mapper(
        componentModel = "spring",
        uses = { ProductMapper.class }, // ✅ để map ProductEntity -> ProductResponse theo mapper bạn viết
        nullValueIterableMappingStrategy = NullValueMappingStrategy.RETURN_NULL,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface CartItemMapper {
//    @Mapping(target = "productId", source = "product.productId")
    CartItemResponse toCartItemResponse(CartItemEntity entity);

    @Mapping(target = "productId", source = "product.productId")
    CartItemRequest toCartItemRequest(CartItemEntity entity);

    List<CartItemResponse> toResponseList(List<CartItemEntity> entities);
}