package com.example.carespawbe.service;

import com.example.carespawbe.dto.Forum.PostCommentResponse;
import com.example.carespawbe.entity.PostComment;
import com.example.carespawbe.mapper.PostCommentMapper;
import com.example.carespawbe.repository.PostCommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostCommentService {

    @Autowired
    private PostCommentRepository postCommentRepository;

    @Autowired
    private PostCommentMapper postCommentMapper;

    public List<PostCommentResponse> getPostCommentsByPostId(Long postId) {
        if (postId != null) {
            List<PostComment> posts = postCommentRepository.findByPostId(postId);
            return postCommentMapper.toCommentResponseList(posts);
        }
        return null;
    }

}
