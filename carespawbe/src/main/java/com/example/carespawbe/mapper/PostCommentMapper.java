package com.example.carespawbe.mapper;

import com.example.carespawbe.dto.Forum.PostCommentResponse;
import com.example.carespawbe.entity.PostComment;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface PostCommentMapper {
    PostCommentResponse toCommentResponse(PostComment postComment);

    List<PostCommentResponse> toCommentResponseList(List<PostComment> postComments);
}
