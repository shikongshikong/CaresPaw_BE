package com.example.carespawbe.service;

import com.example.carespawbe.dto.Forum.ForumPostRequest;
import com.example.carespawbe.dto.Forum.ShortForumPost;
import com.example.carespawbe.entity.ForumPost;
import com.example.carespawbe.repository.ForumPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ForumPostService {

    @Autowired
    private ForumPostRepository forumPostRepository;

    public List<ShortForumPost> searchForumPost(String keyword) {
        List<ShortForumPost> posts = forumPostRepository.searchPost(keyword);

        if (posts.isEmpty()) return null;
        return posts;
    }

    public ForumPost addForumPost(ForumPostRequest forumPostRequest) {
        ForumPost post = ForumPost.builder()
                .title(forumPostRequest.getTitle())
                .content(forumPostRequest.getContent())
                .status(forumPostRequest.getStatus())
                .type(forumPostRequest.getType())
                .build();
        return forumPostRepository.save(post);
    }
}
