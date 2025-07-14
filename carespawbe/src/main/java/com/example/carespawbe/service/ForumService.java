package com.example.carespawbe.service;

import com.example.carespawbe.dto.Forum.ForumPageResponse;
import com.example.carespawbe.repository.ForumPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ForumService {

    @Autowired
    private ForumPostRepository forumPostRepository;

    @Autowired
    private PostHistoryService  postHistoryService;

    @Autowired
    private PostSaveService  postSaveService;

//    @Autowired
//    private

    public ForumPageResponse getForumData(Long userId) {
        ForumPageResponse response = new ForumPageResponse();
//      popular post
        response.setPopularPosts(forumPostRepository.findTop2ByViews(PageRequest.of(0, 2)));
//      list post reverse
        response.setPostList(forumPostRepository.findAllShortByCreateAt());
//      history
        response.setHistoryPosts(postHistoryService.get5PostHistoryByUserId(userId));
//      following
//      save
        response.setSavePosts(postSaveService.get5SavedByUserId(userId));
        return response;
    }
}
