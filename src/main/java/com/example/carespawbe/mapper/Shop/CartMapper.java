package com.example.carespawbe.mapper.Shop;

import com.example.carespawbe.dto.Shop.request.CartRequest;
import com.example.carespawbe.dto.Shop.response.CartResponse;
import com.example.carespawbe.entity.Shop.CartEntity;
import org.mapstruct.*;

import java.util.List;
@Mapper(
        componentModel = "spring",
        uses = { CartItemMapper.class }, // ✅ BẮT BUỘC
        nullValueIterableMappingStrategy = NullValueMappingStrategy.RETURN_NULL,
        unmappedTargetPolicy = ReportingPolicy.IGNORE
)
public interface CartMapper {

    @Mapping(target = "userId", source = "userEntity.id")
//    @Mapping(target = "userFullName", source = "user.fullname")
    @Mapping(target = "cartItems", source = "cartItemEntityList")
    CartResponse toCartResponse(CartEntity entity);

//    @Mapping(target = "user.userId", source = "userId")
//    @Mapping(target = "voucher.voucherId", source = "voucherId")
//    @Mapping(target = "cartItemEntityList", source = "cartItems")
    @Mapping(target = "userId", source = "userEntity.id")
    //    @Mapping(target = "userFullName", source = "user.fullname")
    CartRequest toCartRequest(CartEntity entity);

    List<CartResponse> toResponseList(List<CartEntity> entities);
}
