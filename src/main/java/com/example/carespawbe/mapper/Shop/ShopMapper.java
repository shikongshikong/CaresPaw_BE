package com.example.carespawbe.mapper.Shop;

import com.example.carespawbe.dto.Shop.response.ShopResponse;
import com.example.carespawbe.entity.Shop.ShopEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.NullValueMappingStrategy;

@Mapper(componentModel = "spring", nullValueIterableMappingStrategy = NullValueMappingStrategy.RETURN_NULL)
public interface ShopMapper {
    @Mapping(source = "shopId", target = "shopId")
    @Mapping(source = "shopLogo", target = "shopLogo")
    @Mapping(target = "email", source = "user.email")
    @Mapping(target = "phoneNumber", source = "user.phoneNumber")
    @Mapping(source = "created_at", target = "created_at")
    @Mapping(source = "update_at", target = "updated_at")
    @Mapping(source = "shopDescription", target = "shopDescription")
    @Mapping(source = "shopBanner", target = "shopBanner")
    @Mapping(source = "status", target = "status")

    @Mapping(target = "fullName", source = "user.fullname")
    ShopResponse toResponse(ShopEntity shop);

}
