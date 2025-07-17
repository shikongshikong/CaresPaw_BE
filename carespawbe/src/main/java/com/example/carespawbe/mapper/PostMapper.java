package com.example.carespawbe.mapper;

import com.example.carespawbe.dto.Forum.PostRequest;
import com.example.carespawbe.dto.Forum.PostResponse;
import com.example.carespawbe.entity.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PostMapper {
    @Mapping(source = "userId", target = "user.id")
    Post toPostEntity(PostRequest postRequest);

//    @Mapping(target = "saved", expression = "java(postSave != null)")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.fullname", target = "fullname")
    @Mapping(source = "user.avatar", target = "avatar")
    @Mapping(target = "saved", expression = "java(post.getPostSave() != null)")
    PostResponse toPostResponse(Post post);
}
