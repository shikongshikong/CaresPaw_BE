package com.example.carespawbe.service;

import com.example.carespawbe.dto.Forum.ForumPostRequest;
import com.example.carespawbe.dto.Forum.PostDetailRequest;
import com.example.carespawbe.dto.Forum.PostResponse;
import com.example.carespawbe.dto.Forum.ShortForumPost;
import com.example.carespawbe.entity.ForumPost;
import com.example.carespawbe.mapper.ForumPostMapper;
import com.example.carespawbe.mapper.PostHistoryMapper;
import com.example.carespawbe.mapper.PostSaveMapper;
import com.example.carespawbe.repository.ForumPostRepository;
import com.example.carespawbe.security.JwtAuthenticationFilter;
import com.example.carespawbe.security.JwtService;
import com.example.carespawbe.utils.UserInfo;
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
public class ForumPostService {

    @Autowired
    private ForumPostMapper forumPostMapper;

    @Autowired
    private ForumPostRepository forumPostRepository;

    @Autowired
    private PostHistoryService postHistoryService;

    @Autowired
    private ViewLimiterService viewLimiterService;

    public List<ShortForumPost> getForumPostByKeyword(String keyword, Long userId) {
//        String token = jwtFilter.getJwtFromRequest(request);
//        Long userId = 0L;
//        if (token != null) {
//            userId = jwtService.extractUserId(token);
//            System.out.println("User Id: " + userId);
//            if (userId == null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
//        }
        List<ShortForumPost> posts = forumPostRepository.findByTitleKey(keyword, userId);

        if (posts.isEmpty()) return null;
        return posts;
    }

    public PostResponse addForumPost(ForumPostRequest forumPostRequest) {
        ForumPost post = forumPostMapper.toPostEntity(forumPostRequest);
        try {
            forumPostRepository.save(post);
            return forumPostMapper.toPostResponse(post);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public PostResponse getForumPostById(Long postId, Long userId, HttpServletRequest request) {
        ForumPost post = forumPostRepository.findForumPostById(postId).orElse(null);
        PostDetailRequest postDetailRequest = new PostDetailRequest(postId, userId);

        //      add view for logined user + add history
        if (userId != 0) {
            if (!postHistoryService.isExistHistoryByUserIdAndPostId(userId, postId)) {
                increasePostViewCount(postId);
            }
            postHistoryService.addPostHistory(postDetailRequest);
        }
//      view increasing control
        else {
            String ipAddress = UserInfo.getClientIp(request);

            if (viewLimiterService.isAllowedToView(postId, ipAddress)) {
                increasePostViewCount(postId);
            } else System.out.println("View Exist!");
        }
        return forumPostMapper.toPostResponse(post);
    }

    public List<ShortForumPost> getForumPostListReverse(Long userId) {
//        String token = jwtFilter.getJwtFromRequest(request);
//        Long userId = 0L;
//        if (token != null) {
//            userId = jwtService.extractUserId(token);
//        }
        List<ShortForumPost> posts = forumPostRepository.findAllShortByCreateAt(userId);

        if (posts.isEmpty()) return null;
        return posts;
    }

    public List<ShortForumPost> get2TopPopularPost(Long userId) {
        List<ShortForumPost> posts = forumPostRepository.findTop2ByViews(PageRequest.of(0, 2), userId);

        if (posts.isEmpty()) return null;
        return posts;
    }

    public void increasePostViewCount(Long postId) {
        forumPostRepository.updateViewCount(postId);
    }

    public List<ShortForumPost> getPostListByType(String type, Long userId) {
//        int typeId = 0;
        String typeId = "All";
        switch (type) {
            case "Dog":
//                typeId = 1;
                typeId ="Dog";
                break;
            case "Cat":
//                typeId = 2;
                typeId ="Cat";
                break;
            case "Bird":
//                typeId = 3;
                typeId = "Bird";
                break;
            case "Fish":
//                typeId = 4;
                typeId = "Fish";
                break;
            case "Reptiles":
//                typeId = 5
                typeId = "Reptiles";
                break;
            default:
                break;
        }
        if (typeId.equals("All")) forumPostRepository.findAllShortByCreateAt(userId);
        return forumPostRepository.findForumPostByType(typeId, userId);
    }

}
