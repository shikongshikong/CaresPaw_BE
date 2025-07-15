package com.example.carespawbe.mapper;

import com.example.carespawbe.dto.Forum.ForumPostRequest;
import com.example.carespawbe.dto.Forum.PostResponse;
import com.example.carespawbe.entity.ForumPost;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ForumPostMapper {
    @Mapping(source = "userId", target = "user.id")
    ForumPost toPostEntity(ForumPostRequest forumPostRequest);

//    @Mapping(target = "saved", expression = "java(forumPostSave != null)")
    @Mapping(source = "user.id", target = "userId")
    @Mapping(source = "user.fullname", target = "fullname")
    @Mapping(source = "user.avatar", target = "avatar")
    @Mapping(target = "saved", expression = "java(post.getForumPostSave() != null)")
    PostResponse toPostResponse(ForumPost post);
}
