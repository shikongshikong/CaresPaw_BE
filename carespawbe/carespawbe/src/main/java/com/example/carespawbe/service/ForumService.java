package com.example.carespawbe.service;

import com.example.carespawbe.dto.Forum.ForumPageResponse;
import com.example.carespawbe.dto.Forum.ShortForumPost;
import com.example.carespawbe.dto.History.PostSideBarResponse;
import com.example.carespawbe.repository.ForumPostRepository;
import com.example.carespawbe.security.JwtAuthenticationFilter;
import com.example.carespawbe.security.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ForumService {

//    @Autowired
//    private ForumPostRepository forumPostRepository;

    @Autowired
    private ForumPostService forumPostService;

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
            System.out.println("Save state of 1: " + posts.get(1).isSaved());
            response.setHistoryPosts(posts);
//      following
//      save
            response.setSavePosts(postSaveService.get5SavedByUserId(userId));
        }
        //      popular post
        response.setPopularPosts(forumPostService.get2TopPopularPost(userId));
        //      list post reverse
        response.setPostList(forumPostService.getForumPostListReverse(userId));

        return response;
    }
}
