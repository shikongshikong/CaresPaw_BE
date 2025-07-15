package com.example.carespawbe.mapper;

import com.example.carespawbe.dto.Forum.PostCommentRequest;
import com.example.carespawbe.dto.Forum.PostCommentResponse;
import com.example.carespawbe.entity.PostComment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PostCommentMapper {
    @Mapping(source = "user.avatar", target = "avatar")
    @Mapping(source = "user.fullname", target = "fullname")
    @Mapping(source = "user.id", target = "userId")
    PostCommentResponse toCommentResponse(PostComment postComment);

    List<PostCommentResponse> toCommentResponseList(List<PostComment> postComments);

    @Mapping(source = "userId", target = "user.id")
    @Mapping(source = "postId", target = "post.id")
    PostComment toPostComment(PostCommentRequest postCommentRequest);
}
