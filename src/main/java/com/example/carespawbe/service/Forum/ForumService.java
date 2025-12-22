package com.example.carespawbe.service.Forum;

import com.example.carespawbe.dto.Forum.ForumPageResponse;
import com.example.carespawbe.dto.Forum.ForumPostTrainingDTO;
import com.example.carespawbe.dto.Forum.ForumTrainResponse;
import com.example.carespawbe.dto.History.ForumHistoryTrainDTO;
import com.example.carespawbe.dto.History.ForumPostHistoryTagResponse;
import com.example.carespawbe.dto.Like.LikeTrainDTO;
import com.example.carespawbe.entity.Forum.ForumPostHistoryEntity;
import com.example.carespawbe.entity.Forum.ForumPostLikeEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ForumService {

    @Autowired
    private ForumPostService forumPostService;

    @Autowired
    private ForumPostHistoryService forumPostHistoryService;

    @Autowired
    private ForumPostSaveService forumPostSaveService;

    @Autowired
    private ForumPostLikeService forumPostLikeService;
    //
    // @Autowired
    // JwtService jwtService;
    //
    // @Autowired
    // JwtAuthenticationFilter jwtFilter;

    // public ForumPageResponse getForumData(Long userId) {
    // ForumPageResponse response = new ForumPageResponse();
    //
    // if (userId != 0L) {
    //// history
    // List<ForumPostHistoryTagResponse> posts =
    // forumPostHistoryService.get5PostHistoryByUserId(userId);
    // if (posts != null) {
    // response.setHistoryPosts(posts);
    // } else {
    // System.out.println("NUll in history");
    // response.setHistoryPosts(null);
    // }
    //// following
    //// save
    //// response.setSavePosts(forumPostSaveService.get5SavedByUserId(userId));
    // }
    // // popular forumPostEntity
    // response.setPopularPosts(forumPostService.get2TopPopularPost(userId));
    // // list forumPostEntity reverse
    // response.setPostList(forumPostService.getForumPostListReverse(userId));
    //
    // return response;
    // }
    public ForumPageResponse getForumData(Long userId, boolean includeHistory, int page) {
        ForumPageResponse response = new ForumPageResponse();

        // history
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

        // popular forumPostEntity
//        if (includePopular) {
//            response.setPopularPosts(forumPostService.get2TopPopularPost(userId));
//        }

        // list forumPostEntity by page
        int size = 5;
        response.setPostList(forumPostService.getForumPostByPage(userId, page, size));

        return response;
    }

    public ForumTrainResponse getForumTrainData() {
        ForumTrainResponse forumTrainResponse = new ForumTrainResponse();

        List<ForumPostTrainingDTO> trainingDTOS = forumPostService.getForumPostsGroupCategory();
        forumTrainResponse.setPostDb(trainingDTOS);

        List<ForumHistoryTrainDTO> historyEntities = forumPostHistoryService.getAllHistory();
        forumTrainResponse.setPostHistoryDb(historyEntities);

        List<LikeTrainDTO> likeEntities = forumPostLikeService.getAllLike();
        forumTrainResponse.setPostLikeDb(likeEntities);

        return forumTrainResponse;
    }
}
