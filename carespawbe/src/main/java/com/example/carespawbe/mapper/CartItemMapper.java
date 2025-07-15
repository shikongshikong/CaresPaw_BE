//package com.example.carespawbe.mapper;
//
//import com.example.carespawbe.dto.request.CartItemRequest;
//import com.example.carespawbe.dto.response.CartItemResponse;
//import com.example.carespawbe.entity.shop.CartItemEntity;
//import org.mapstruct.*;
//
//import java.util.List;
//
//@Mapper(componentModel = "spring", nullValueIterableMappingStrategy = NullValueMappingStrategy.RETURN_NULL)
//public interface CartItemMapper {
//
//    @Mapping(target = "productId", source = "product.productId")
////    @Mapping(target = "productName", source = "product.productName")
//    @Mapping(target = "productVarriantId", source = "productVarriant.productVarriantId")
////    @Mapping(target = "variantName", source = "productVariant.variantName")
//    CartItemResponse toCartItemResponse(CartItemEntity entity);
//
////    @Mapping(target = "product.productId", source = "productId")
////    @Mapping(target = "productVarriant.productVarriantId", source = "productVarriantId")
//    @Mapping(target = "productId", source = "product.productId")
//    @Mapping(target = "productVarriantId", source = "productVarriant.productVarriantId")
//    CartItemRequest toCartItemRequest(CartItemEntity entity);
//
//    List<CartItemResponse> toResponseList(List<CartItemEntity> entities);
//}
