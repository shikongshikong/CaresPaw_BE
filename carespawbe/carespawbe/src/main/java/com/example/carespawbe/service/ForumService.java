package com.example.carespawbe.service;

import com.example.carespawbe.dto.Forum.ForumPageResponse;
import com.example.carespawbe.dto.History.ForumPostHistoryTagResponse;
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
//
//    @Autowired
//    JwtService jwtService;
//
//    @Autowired
//    JwtAuthenticationFilter jwtFilter;

//    public ForumPageResponse getForumData(Long userId) {
//        ForumPageResponse response = new ForumPageResponse();
//
//        if (userId != 0L) {
////      history
//            List<ForumPostHistoryTagResponse> posts = forumPostHistoryService.get5PostHistoryByUserId(userId);
//            if (posts != null) {
//                response.setHistoryPosts(posts);
//            } else {
//                System.out.println("NUll in history");
//                response.setHistoryPosts(null);
//            }
////      following
////      save
////            response.setSavePosts(forumPostSaveService.get5SavedByUserId(userId));
//        }
//        //      popular forumPostEntity
//        response.setPopularPosts(forumPostService.get2TopPopularPost(userId));
//        //      list forumPostEntity reverse
//        response.setPostList(forumPostService.getForumPostListReverse(userId));
//
//        return response;
//    }
public ForumPageResponse getForumData(Long userId, boolean includePopular, boolean includeHistory, int page) {
    ForumPageResponse response = new ForumPageResponse();

    //  history
    if (userId != 0L) {
        if (includeHistory) {
            List<ForumPostHistoryTagResponse> posts = forumPostHistoryService.get5PostHistoryByUserId(userId);
            if (posts != null) {
                response.setHistoryPosts(posts);
            } else {
                System.out.println("NUll in history");
                response.setHistoryPosts(null);
            }
        }
    }

    //  popular forumPostEntity
    if (includePopular) {
        response.setPopularPosts(forumPostService.get2TopPopularPost(userId));
    }

    //  list forumPostEntity by page
    int size = 5;
    response.setPostList(forumPostService.getForumPostByPage(page, size));

    return response;
}
}

//        String token = jwtFilter.getJwtFromRequest(request);
//        Long userId = 0L;
//        if (token != null) {
//            userId = jwtService.extractUserId(token);
//            System.out.println("UserEntity Id: " + userId);
//            if (userId == null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
//        }
