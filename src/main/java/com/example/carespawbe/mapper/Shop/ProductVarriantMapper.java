//package com.example.carespawbe.mapper.Shop;
//
//import com.example.carespawbe.dto.Shop.response.ProductVarriantResponse;
//import com.example.carespawbe.entity.Shop.ProductVarriantEntity;
//import org.mapstruct.Mapper;
//import org.mapstruct.Mapping;
//
//import java.util.List;
//
//@Mapper(componentModel = "spring")
//public interface ProductVarriantMapper {
//
//    @Mapping(target = "varriantId", source = "varriants.varriantId")
//    @Mapping(target = "varriantName", source = "varriants.varriantName")
//    @Mapping(target = "varriantValueId", source = "varriantValue.varriantValueId")
//    @Mapping(target = "valueName", source = "varriantValue.valueName")
//    ProductVarriantResponse toResponse(ProductVarriantEntity entity);
//
//    List<ProductVarriantResponse> toResponseList(List<ProductVarriantEntity> entities);
//}
