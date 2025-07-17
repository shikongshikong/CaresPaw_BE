package com.example.carespawbe.service;

import com.example.carespawbe.dto.Forum.*;
import com.example.carespawbe.entity.Post;
import com.example.carespawbe.mapper.PostMapper;
import com.example.carespawbe.repository.PostRepository;
import com.example.carespawbe.utils.UserInfo;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService {

    @Autowired
    private PostMapper postMapper;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostHistoryService postHistoryService;

    @Autowired
    private PostCategoryService postCategoryService;

    @Autowired
    private ViewLimiterService viewLimiterService;

    public List<ShortForumPost> getForumPostByKeyword(String keyword, Long userId) {

        List<ShortForumPost> posts = postRepository.findByTitleKey(keyword, userId);

        if (posts.isEmpty()) return null;
        return posts;
    }

    public PostResponse addForumPost(PostRequest postRequest) {
        Post post = postMapper.toPostEntity(postRequest);
        try {
            postRepository.save(post);
//            control category
            List<Integer> arr = postRequest.getSelectedCategories();
            Long postId = post.getId();

            System.out.println("List of Category Id: " + arr);

            List<PostCategoryRequest> postCategoryRequestList = new ArrayList<>();

            for (Integer caId : arr) {
                PostCategoryRequest categoryRequest = PostCategoryRequest
                        .builder()
                        .postId(postId)
                        .categoryId(caId)
                        .build();
                System.out.println("Each Category Id: " + caId);
                postCategoryRequestList.add(categoryRequest);
            }

            postCategoryService.addCategoryList(postCategoryRequestList);


            return postMapper.toPostResponse(post);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public PostResponse getForumPostById(Long postId, Long userId, HttpServletRequest request) {
        Post post = postRepository.findForumPostById(postId).orElse(null);
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
        return postMapper.toPostResponse(post);
    }

    public List<ShortForumPost> getForumPostListReverse(Long userId) {
//        String token = jwtFilter.getJwtFromRequest(request);
//        Long userId = 0L;
//        if (token != null) {
//            userId = jwtService.extractUserId(token);
//        }
        List<ShortForumPost> posts = postRepository.findAllShortByCreateAt(userId);

        if (posts.isEmpty()) return null;
        return posts;
    }

    public List<ShortForumPost> get2TopPopularPost(Long userId) {
        List<ShortForumPost> posts = postRepository.findTop2ByViews(PageRequest.of(0, 2), userId);

        if (posts.isEmpty()) return null;
        return posts;
    }

    public void increasePostViewCount(Long postId) {
        postRepository.updateViewCount(postId);
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
        if (typeId.equals("All")) postRepository.findAllShortByCreateAt(userId);
        return postRepository.findForumPostByType(typeId, userId);
    }

    public List<ShortForumPost> getForumPostByCategory(List<Integer> category, Long userId, String type) {
        if (category.isEmpty()) return null;
        if (type.equals("All")) return postRepository.findPostsByCategory(userId, category);
        return postRepository.findPostsByTypeAndCategory(userId, type, category);
    }

}
