package com.example.carespawbe.service;

import com.example.carespawbe.dto.Forum.ForumPageResponse;
import com.example.carespawbe.dto.History.PostSideBarResponse;
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
//    private PostRepository forumPostRepository;

    @Autowired
    private PostService postService;

    @Autowired
    private PostHistoryService  postHistoryService;

    @Autowired
    private PostSaveService  postSaveService;

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
//            System.out.println("User Id: " + userId);
//            if (userId == null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
//        }
        if (userId != 0L) {
//      history
            List<PostSideBarResponse> posts = postHistoryService.get5PostHistoryByUserId(userId);
//            System.out.println("Save state of 1: " + posts.get(1).isSaved());
            if (posts != null) {
                response.setHistoryPosts(posts);
            } else {
                System.out.println("NUll in history");
                response.setHistoryPosts(null);
            }
//      following
//      save
            response.setSavePosts(postSaveService.get5SavedByUserId(userId));
        }
        //      popular post
        response.setPopularPosts(postService.get2TopPopularPost(userId));
        //      list post reverse
        response.setPostList(postService.getForumPostListReverse(userId));

        return response;
    }
}
