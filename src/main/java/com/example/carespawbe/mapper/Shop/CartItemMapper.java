package com.example.carespawbe.mapper.Shop;

import com.example.carespawbe.dto.Shop.request.CartItemRequest;
import com.example.carespawbe.dto.Shop.response.CartItemResponse;
import com.example.carespawbe.entity.Shop.CartItemEntity;
import org.mapstruct.*;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = { ProductMapper.class },
        nullValueIterableMappingStrategy = NullValueMappingStrategy.RETURN_NULL,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface CartItemMapper {

    // ✅ entity String -> response String (MapStruct auto map)
    CartItemResponse toCartItemResponse(CartItemEntity entity);

//    @Mapping(target = "productId", source = "product.productId")
//    CartItemRequest toCartItemRequest(CartItemEntity entity);

    List<CartItemResponse> toResponseList(List<CartItemEntity> entities);
}
