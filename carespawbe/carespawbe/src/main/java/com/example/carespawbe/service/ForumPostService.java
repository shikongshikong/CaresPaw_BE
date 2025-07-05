package com.example.carespawbe.service;

import com.example.carespawbe.dto.Forum.ForumPostRequest;
import com.example.carespawbe.dto.Forum.PostResponse;
import com.example.carespawbe.dto.Forum.ShortForumPost;
import com.example.carespawbe.entity.ForumPost;
import com.example.carespawbe.mapper.ForumPostMapper;
import com.example.carespawbe.repository.ForumPostRepository;
import com.example.carespawbe.utils.UserInfo;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ForumPostService {

    @Autowired
    private ForumPostMapper  forumPostMapper;

    @Autowired
    private ForumPostRepository forumPostRepository;

    @Autowired
    private ViewLimiterService viewLimiterService;

    public List<ShortForumPost> getForumPostByKeyword(String keyword) {
        List<ShortForumPost> posts = forumPostRepository.findByTitleKey(keyword);

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

    public PostResponse getForumPostById(Long postId, HttpServletRequest request) {
        ForumPost post = forumPostRepository.findForumPostById(postId).orElse(null);

//      view increasing control
        String ipAddress = UserInfo.getClientIp(request);
        if (viewLimiterService.isAllowedToView(postId, ipAddress)) {
            increasePostViewCount(postId);
        } else System.out.println("View Exist!");
        return forumPostMapper.toPostResponse(post);
    }

    public List<ShortForumPost> getForumPostListReverse() {
        List<ShortForumPost> posts = forumPostRepository.findAllShortByCreateAt();

        if (posts.isEmpty()) return null;
        return posts;
    }

    public void increasePostViewCount(Long postId) {
        forumPostRepository.updateViewCount(postId);
    }

}
