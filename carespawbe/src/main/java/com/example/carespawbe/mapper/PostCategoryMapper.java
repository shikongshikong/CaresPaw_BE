package com.example.carespawbe.mapper;

import com.example.carespawbe.dto.Forum.PostCategoryRequest;
import com.example.carespawbe.entity.PostBelongToCategory;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PostCategoryMapper {

    List<PostBelongToCategory> toPocategoryList(List<PostCategoryRequest> postCategoryRequestList);

    @Mapping(source = "postId", target = "post.id")
    @Mapping(source = "categoryId", target = "postCategory.id")
    PostBelongToCategory toPostCategoryEntity(PostCategoryRequest postCategoryRequest);

}
