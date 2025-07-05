package com.example.carespawbe.service;

import com.example.carespawbe.dto.Forum.ForumPageResponse;
import com.example.carespawbe.repository.ForumPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ForumService {

    @Autowired
    private ForumPostRepository forumPostRepository;

//    @Autowired
//    private

    public ForumPageResponse getForumData() {
        ForumPageResponse response = new ForumPageResponse();

//      popular post
        response.setPopularPosts(forumPostRepository.findTop2ByViews(PageRequest.of(0, 2)));
//      list post reverse
        response.setPostList(forumPostRepository.findAllShortByCreateAt());
//      history
//      following
//      save
        return response;
    }
}
