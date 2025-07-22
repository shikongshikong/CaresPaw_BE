package com.example.carespawbe.service;

import com.example.carespawbe.dto.Forum.ForumPageResponse;
import com.example.carespawbe.dto.History.ForumPostSideBarResponse;
import com.example.carespawbe.security.JwtAuthenticationFilter;
import com.example.carespawbe.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ForumService {

//    @Autowired
//    private ForumPostRepository forumPostRepository;

    @Autowired
    private ForumPostService forumPostService;

    @Autowired
    private ForumPostHistoryService forumPostHistoryService;

    @Autowired
    private ForumPostSaveService forumPostSaveService;

    @Autowired
    JwtService jwtService;

    @Autowired
    JwtAuthenticationFilter jwtFilter;

    public ForumPageResponse getForumData(Long userId) {
        ForumPageResponse response = new ForumPageResponse();

//        String token = jwtFilter.getJwtFromRequest(request);
//        Long userId = 0L;
//        if (token != null) {
//            userId = jwtService.extractUserId(token);
//            System.out.println("UserEntity Id: " + userId);
//            if (userId == null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
//        }
        if (userId != 0L) {
//      history
            List<ForumPostSideBarResponse> posts = forumPostHistoryService.get5PostHistoryByUserId(userId);
//            System.out.println("Save state of 1: " + forumPostEntities.get(1).isSaved());
            if (posts != null) {
                response.setHistoryPosts(posts);
            } else {
                System.out.println("NUll in history");
                response.setHistoryPosts(null);
            }
//      following
//      save
            response.setSavePosts(forumPostSaveService.get5SavedByUserId(userId));
        }
        //      popular forumPostEntity
        response.setPopularPosts(forumPostService.get2TopPopularPost(userId));
        //      list forumPostEntity reverse
        response.setPostList(forumPostService.getForumPostListReverse(userId));

        return response;
    }
}
