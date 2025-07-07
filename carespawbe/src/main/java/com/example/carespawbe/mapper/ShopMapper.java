package com.example.carespawbe.mapper;

import com.example.carespawbe.dto.response.ShopResponse;
import com.example.carespawbe.entity.shop.ShopEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueMappingStrategy;

@Mapper(componentModel = "spring", nullValueIterableMappingStrategy = NullValueMappingStrategy.RETURN_NULL)
public interface ShopMapper {
    @Mapping(source = "shopId", target = "shopId")
    @Mapping(source = "shopLogo", target = "shopLogo")
    @Mapping(target = "email", source = "user.email")
    @Mapping(target = "phoneNumber", source = "user.phoneNumber")
    ShopResponse toResponse(ShopEntity shop);

}
