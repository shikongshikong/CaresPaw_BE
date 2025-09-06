package com.example.carespawbe.service;

import com.example.carespawbe.dto.Forum.*;
import com.example.carespawbe.entity.ForumPostEntity;
import com.example.carespawbe.mapper.ForumPostMapper;
import com.example.carespawbe.repository.ForumPostRepository;
import com.example.carespawbe.utils.UserInfo;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ForumPostService {

    @Autowired
    private ForumPostMapper postMapper;

    @Autowired
    private ForumPostRepository forumPostRepository;

    @Autowired
    private ForumPostHistoryService forumPostHistoryService;

    @Autowired
    private ForumPostCategoryService forumPostCategoryService;

    @Autowired
    private ViewLimiterService viewLimiterService;

    public List<ShortForumPostResponse> getForumPostByKeyword(String keyword, Long userId) {

        List<ShortForumPostResponse> posts = forumPostRepository.findByTitleKey(keyword, userId);

        if (posts.isEmpty()) return null;
        return posts;
    }

    public ForumPostResponse addForumPost(ForumPostRequest forumPostRequest) {
        ForumPostEntity forumPostEntity = postMapper.toPostEntity(forumPostRequest);
        try {
            forumPostRepository.save(forumPostEntity);
//            control category
            List<Integer> arr = forumPostRequest.getSelectedCategoryList();
            Long postId = forumPostEntity.getId();

            System.out.println("List of Category Id: " + arr);
            if (arr != null) {
                List<ForumPostCategoryRequest> forumPostCategoryRequestList = new ArrayList<>();

                for (Integer caId : arr) {
                    ForumPostCategoryRequest categoryRequest = ForumPostCategoryRequest
                            .builder()
                            .postId(postId)
                            .categoryId(caId)
                            .build();
                    System.out.println("Each Category Id: " + caId);
                    forumPostCategoryRequestList.add(categoryRequest);
                }

                forumPostCategoryService.addCategoryList(forumPostCategoryRequestList);
            }

            return postMapper.toPostResponse(forumPostEntity);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ForumPostResponse getForumPostById(Long postId, Long userId, HttpServletRequest request) {
        ForumPostEntity forumPostEntity = forumPostRepository.findForumPostById(postId).orElse(null);
        ForumPostDetailRequest forumPostDetailRequest = new ForumPostDetailRequest(postId, userId);

        //      add view for logined userEntity + add history
        if (userId != 0) {
            if (!forumPostHistoryService.isExistHistoryByUserIdAndPostId(userId, postId)) {
                increasePostViewCount(postId);
            }
            forumPostHistoryService.addPostHistory(forumPostDetailRequest);
        }
//      view increasing control
        else {
            String ipAddress = UserInfo.getClientIp(request);

            if (viewLimiterService.isAllowedToView(postId, ipAddress)) {
                increasePostViewCount(postId);
            } else System.out.println("View Exist!");
        }
        return postMapper.toPostResponse(forumPostEntity);
    }

//    public List<ShortForumPostResponse> getForumPostListReverse(Long userId) {
////        String token = jwtFilter.getJwtFromRequest(request);
////        Long userId = 0L;
////        if (token != null) {
////            userId = jwtService.extractUserId(token);
////        }
//        List<ShortForumPostResponse> posts = forumPostRepository.findAllShortByCreateAt(userId);
//
//        if (posts.isEmpty()) return null;
//        return posts;
//    }

    public List<ShortForumPostResponse> getForumPostListByPage(Long userId, Long page, int size) {
        List<ShortForumPostResponse> posts = forumPostRepository.findAllShortByCreateAt(userId);

        if (posts.isEmpty()) return null;
        return posts;
    }

//    public Page<ForumPostEntity> getForumPostByPage(int page, int size) {
//        return forumPostRepository.findAll(PageRequest.of(page - 1, size));
//    }


    public List<ShortForumPostResponse> get2TopPopularPost(Long userId) {
        List<ShortForumPostResponse> posts = forumPostRepository.findTop2ByViews(PageRequest.of(0, 2), userId);

        if (posts.isEmpty()) return null;
        return posts;
    }

    public void increasePostViewCount(Long postId) {
        forumPostRepository.updateViewCount(postId);
    }

    public List<ShortForumPostResponse> getPostListByType(int typeId, Long userId) {
//        int typeId = 0;
//        switch (type) {
//            case "Dog":
//                typeId = 1;
////                typeId ="Dog";
//                break;
//            case "Cat":
////                typeId = 2;
//                typeId ="Cat";
//                break;
//            case "Bird":
////                typeId = 3;
//                typeId = "Bird";
//                break;
//            case "Fish":
////                typeId = 4;
//                typeId = "Fish";
//                break;
//            case "Reptiles":
////                typeId = 5
//                typeId = "Reptiles";
//                break;
//            default:
//                break;
//        }
        if (typeId == 0) forumPostRepository.findAllShortByCreateAt(userId);
        return forumPostRepository.findForumPostByType(typeId, userId);
    }

    public List<ShortForumPostResponse> getForumPostByCategory(List<Integer> category, Long userId, String type) {
        if (category.isEmpty()) return null;
        if (type.equals("All")) return forumPostRepository.findPostsByCategory(userId, category);
        return forumPostRepository.findPostsByTypeAndCategory(userId, type, category);
    }

    public List<ForumPostEntity> getPostListByUserId(Long userId) {
        List<ForumPostEntity> forumPostEntities = forumPostRepository.findByUserId(userId);
        if (forumPostEntities.isEmpty()) return null;
        return forumPostEntities;
    }

    public int updatePostInfo(Long postId, ForumPostRequest forumPostRequest) {
        return forumPostRepository.updatePost(postId, forumPostRequest.getTitle(), forumPostRequest.getContent()
                ,LocalDate.now(), forumPostRequest.getState());
    }

    public int deletePost(Long postId) {
        return forumPostRepository.removePostById(postId);
    }

    public void increaseCommentCount(Long postId) {
        forumPostRepository.updateCmCount(postId);
    }

    public Page<ForumPostEntity> getForumPostByPage(int page, int size) {
        return forumPostRepository.findAll(PageRequest.of(page - 1, size));
    }

}
