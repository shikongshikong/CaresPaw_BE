package com.example.carespawbe.mapper;

import com.example.carespawbe.dto.request.CartRequest;
import com.example.carespawbe.dto.response.CartResponse;
import com.example.carespawbe.entity.shop.CartEntity;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", nullValueIterableMappingStrategy = NullValueMappingStrategy.RETURN_NULL)
public interface CartMapper {

    @Mapping(target = "userId", source = "user.id")
//    @Mapping(target = "userFullName", source = "user.fullname")
    @Mapping(target = "voucherId", source = "voucher.voucherId")
    @Mapping(target = "voucherName", source = "voucher.voucherName")
    CartResponse toCartResponse(CartEntity entity);

//    @Mapping(target = "user.userId", source = "userId")
//    @Mapping(target = "voucher.voucherId", source = "voucherId")
//    @Mapping(target = "cartItemEntityList", source = "cartItems")
    @Mapping(target = "userId", source = "user.id")
    //    @Mapping(target = "userFullName", source = "user.fullname")
    @Mapping(target = "voucherId", source = "voucher.voucherId")
    @Mapping(target = "voucherName", source = "voucher.voucherName")
    CartRequest toCartRequest(CartEntity entity);

    List<CartResponse> toResponseList(List<CartEntity> entities);
}
