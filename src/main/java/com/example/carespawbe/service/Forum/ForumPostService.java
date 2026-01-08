package com.example.carespawbe.service.Forum;

import com.example.carespawbe.dto.Forum.*;
import com.example.carespawbe.entity.Forum.ForumPostEntity;
import com.example.carespawbe.entity.Forum.ForumPostTypeEntity;
import com.example.carespawbe.mapper.Forum.ForumPostMapper;
import com.example.carespawbe.repository.Forum.ForumPostRepository;
import com.example.carespawbe.utils.UserInfo;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    @Autowired
    private PostRecommendationService postRecommendationService;

    @Autowired
    private ForumPostTypeService forumPostTypeService;

    public List<ForumPostTrainingDTO> getForumPostsGroupCategory(){
        List<ForumPostEntity>  forumPostEntities = forumPostRepository.findAllWithCategories();
        List<ForumPostTrainingDTO> forumPostTrainingDTOs = forumPostEntities.stream()
                .map(p -> new ForumPostTrainingDTO(
                        p.getId(),
                        p.getTitle(),
                        p.getContent(),
                        p.getCreateAt(),
                        p.getState(),
                        p.getTypeEntity().getId(),
                        p.getToCategories().stream().map(tc -> tc.getForumPostCategoryEntity().getId())
                                .distinct()
                                .toList()
                )).toList();
        return forumPostTrainingDTOs;
    }

    public List<ShortForumPostResponse> getForumPostByKeyword(String keyword, Long userId) {
//        int size = 5;
        List<ShortForumPostResponse> posts = forumPostRepository.findByTitleKey(keyword, userId);

        if (posts.isEmpty()) return null;
        return posts;
    }

    public ForumPostResponse addForumPost(ForumPostRequest forumPostRequest) {
        ForumPostEntity forumPostEntity = postMapper.toPostEntity(forumPostRequest);
        ForumPostTypeEntity typeEntity = forumPostTypeService.getForumPostTypeById(forumPostRequest.getTypeId());
        forumPostEntity.setTypeEntity(typeEntity);
        try {
            forumPostRepository.save(forumPostEntity);
//          control category
            List<Integer> arr = forumPostRequest.getSelectedCategoryList();
            Long postId = forumPostEntity.getId();

            System.out.println("List of Category Id: " + arr);
            if (arr != null) {
                List<ForumPostCategoryRequest> forumPostCategoryRequestList = new ArrayList<>();

                for (int caId : arr) {
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
//        ForumPostEntity forumPostEntity = forumPostRepository.findForumPostById(postId).orElse(null);
        ForumPostResponse forumPostResponse = forumPostRepository.findForumPostDetailById(userId, postId).orElse(null);
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
//        return postMapper.toPostResponse(forumPostEntity);
        return forumPostResponse;
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

    public Page<ShortForumPostResponse> getForumPostListByPage(Long userId, int page) {
        int size = 5;
        Page<ShortForumPostResponse> posts = forumPostRepository.findPageShortByCreateAt(userId, PageRequest.of(page - 1, size));

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

//    public List<ShortForumPostResponse> getPostListByType(int typeId, Long userId) {
////        int typeId = 0;
////        switch (type) {
////            case "Dog":
////                typeId = 1;
//////                typeId ="Dog";
////                break;
////            case "Cat":
//////                typeId = 2;
////                typeId ="Cat";
////                break;
////            case "Bird":
//////                typeId = 3;
////                typeId = "Bird";
////                break;
////            case "Fish":
//////                typeId = 4;
////                typeId = "Fish";
////                break;
////            case "Reptiles":
//////                typeId = 5
////                typeId = "Reptiles";
////                break;
////            default:
////                break;
////        }
//        if (typeId == 0) forumPostRepository.findAllShortByCreateAt(userId);
//        return forumPostRepository.findForumPostByType(typeId, userId);
//    }

//    public List<ShortForumPostResponse> getForumPostByCategory(List<Integer> category, Long userId, String type) {
//        if (category.isEmpty()) return null;
//        if (type.equals("All")) return forumPostRepository.findPostsByCategory(userId, category);
//        return forumPostRepository.findPostsByTypeAndCategory(userId, type, category);
//    }

    public List<ForumPostEntity> getPostListByUserId(Long userId) {
        List<ForumPostEntity> forumPostEntities = forumPostRepository.findByUserId(userId);
        if (forumPostEntities.isEmpty()) return null;
        return forumPostEntities;
    }

//    public int updatePostInfo(Long postId, ForumPostRequest forumPostRequest) {
//        return forumPostRepository.updatePost(postId, forumPostRequest.getTitle(), forumPostRequest.getContent()
//                ,LocalDate.now(), forumPostRequest.getState());
//    }

    public int deletePost(Long postId) {
        return forumPostRepository.removePostById(postId);
    }

    public void increaseCommentCount(Long postId) {
        forumPostRepository.updateCmCount(postId);
    }

    public Page<ShortForumPostResponse> getForumPostByPage(Long userId, int page, int size) {
        System.out.println("User id in post service: " + userId);
        return forumPostRepository.findPageShortByCreateAt(userId, PageRequest.of(page - 1, size));
    }

//    public Page<ShortForumPostResponse> getPostListByType(int typeId, Long userId, int page) {
//        int size = 5;
//
//        if (typeId == 0) forumPostRepository.findPageShortByCreateAt(userId, PageRequest.of(page - 1, size));
//        return forumPostRepository.findPageShortsByType(typeId, userId, PageRequest.of(page - 1, size));
//    }

    public ForumPostEntity updateForumPost(Long postId, ForumPostUpdateRequest forumPostUpdateRequest) {
        ForumPostEntity forumPostEntity = forumPostRepository.findForumPostById(postId).orElse(null);
        if (forumPostEntity == null) return null;

        forumPostEntity.setTitle(forumPostUpdateRequest.getTitle());
        forumPostEntity.setContent(forumPostUpdateRequest.getContent());
        forumPostEntity.setState(forumPostUpdateRequest.getState());
        forumPostEntity.setTypeEntity(forumPostTypeService.getForumPostTypeById(forumPostUpdateRequest.getTypeId()));
        forumPostEntity.setUpdateAt(LocalDate.now());

        return forumPostRepository.save(forumPostEntity);
    }

    public ForumPageFilterResponse getPostListByTypeAndCategories(Long userId, int page, int typeId, List<Integer> categoryList, String colFilter) {
        int size = 5;
        ForumPageFilterResponse response = new ForumPageFilterResponse();

        String colPageabe = "createAt";

        switch (colFilter) {
            case "newest":
                colPageabe = "createAt";
                break;
//            case "recommend":
//                colPageabe = "createAt";
//                break;
            case "popular":
                colPageabe = "viewedAmount";
                break;
            case "comments":
                colPageabe = "commentedAmount";
                break;
            default:
                colPageabe = "createAt";
        }

        Pageable pageable = PageRequest.of(page - 1, size, Sort.by(colPageabe).descending());

        if (typeId == 0 && !categoryList.isEmpty())
            response.setPostList(forumPostRepository.findPageShortsByCategory(userId, categoryList, pageable));
        else if (typeId != 0 && categoryList.isEmpty())
            response.setPostList(forumPostRepository.findPageShortsByType(userId, typeId, pageable));
        else if (typeId == 0 && categoryList.isEmpty())
            response.setPostList(forumPostRepository.findPageShortByCreateAt(userId, pageable));
        else
//            response.setPostList(forumPostRepository.findPageShortByTypeAndToCategories(userId, typeId, categoryList, (Pageable) Sort.by("createAt").descending()));
//            pageable = switch (colFilter) {
//                case "newest" -> (Pageable) Sort.by("createAt").descending();
//                case "popular" -> (Pageable) Sort.by("viewedAmount").descending();
//                case "comments" -> (Pageable) Sort.by("commentedAmount").descending();
//                default -> PageRequest.of(page - 1, size, Sort.by("createAt").descending());
//            };
//            response.setPostList(forumPostRepository.findPageShortByTypeAndToCategories(userId, typeId, categoryList, pageable));
            switch (colFilter) {
                case "newest":
                    response.setPostList(forumPostRepository.findPageShortByTypeAndToCategories(userId, typeId, categoryList, pageable));
                    break;
//                case "recommend":
//                    response.setPostList(new ForumPostService.getRecommendedPosts());
//                    break;
                case "popular":
                    response.setPostList(forumPostRepository.findPageShortByTypeAndToCategories(userId, typeId, categoryList, pageable));
                    break;
                case "comments":
                    response.setPostList(forumPostRepository.findPageShortByTypeAndToCategories(userId, typeId, categoryList, pageable));
                    break;
                default:
                    response.setPostList(forumPostRepository.findPageShortByTypeAndToCategories(userId, typeId, categoryList, pageable));
            }
        return response;
    }

    public List<ShortForumPostResponse> getRecommendedPosts(Long userId) {
        List<Long> postIds = postRecommendationService.getRecommendedPosts(userId);
        List<ShortForumPostResponse> shortForumPostResponses = new ArrayList<>();
        if (postIds.isEmpty()) return null;
        for (Long postId : postIds) {
            System.out.println("postId: " + postId);
            ShortForumPostResponse shortForumPost = forumPostRepository.findByPostId(postId, userId);
            if (shortForumPost == null) continue;
            shortForumPostResponses.add(shortForumPost);
        }
        return shortForumPostResponses;
    }

    public List<ShortSimilarPost> getSimilarPosts(Long postId) {
        List<Long> postIds = postRecommendationService.getSimilarPosts(postId);
        List<ShortSimilarPost> shortSimilarPosts = new ArrayList<>();
        if (postIds.isEmpty()) return null;
        for (Long similarId : postIds) {
            System.out.println("postId: " + similarId);
            Optional<ForumPostEntity> similarPost = forumPostRepository.findForumPostById(similarId);
            if (similarPost.isEmpty()) continue;
            shortSimilarPosts.add(postMapper.toShortSimilarPost(similarPost.get()));
        }
        return shortSimilarPosts;
    }
}
