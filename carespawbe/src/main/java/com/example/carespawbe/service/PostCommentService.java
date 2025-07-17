package com.example.carespawbe.service;

import com.example.carespawbe.dto.Forum.PostCommentRequest;
import com.example.carespawbe.dto.Forum.PostCommentResponse;
import com.example.carespawbe.entity.ForumPost;
import com.example.carespawbe.entity.PostComment;
import com.example.carespawbe.entity.User;
import com.example.carespawbe.mapper.PostCommentMapper;
import com.example.carespawbe.repository.ForumPostRepository;
import com.example.carespawbe.repository.PostCommentRepository;
import com.example.carespawbe.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostCommentService {

    @Autowired
    private PostCommentRepository postCommentRepository;

    @Autowired
    private ForumPostRepository forumPostRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostCommentMapper postCommentMapper;

    public PostCommentResponse addPostComment(PostCommentRequest postCommentRequest) {
//        ForumPost post = forumPostRepository.findById(postCommentRequest.getPostId()).orElse(null);
//        User user = userRepository.findById(postCommentRequest.getUserId()).orElse(null);
        PostComment cm = postCommentMapper.toPostComment(postCommentRequest);
//        cm.setPost(post);
//        cm.setUser(user);
        postCommentRepository.save(cm);
        return postCommentMapper.toCommentResponse(cm);
    }

    public List<PostCommentResponse> getPostCommentsByPostId(Long postId) {
        if (postId != null) {
            List<PostComment> posts = postCommentRepository.findByPostId(postId);
            return postCommentMapper.toCommentResponseList(posts);
        }
        return null;
    }

}
