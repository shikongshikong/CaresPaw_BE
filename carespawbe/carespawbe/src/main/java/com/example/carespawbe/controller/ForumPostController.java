package com.example.carespawbe.controller;

import com.example.carespawbe.dto.Forum.*;
import com.example.carespawbe.dto.Save.SaveStatusUpdateRequest;
import com.example.carespawbe.service.*;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/forum")
@CrossOrigin(origins = "http://localhost:3000")
public class ForumPostController {

    @Autowired
    private ForumPostService forumPostService;

    @Autowired
    private ForumService forumService;

    @Autowired
    private ForumPostSaveService forumPostSaveService;

    @Autowired
    private ForumPostCommentService forumPostCommentService;

    @Autowired
    private ForumPostLikeService forumPostLikeService;

//    @GetMapping("")
//    public ResponseEntity<?> getForumData(HttpServletRequest request) {
////        String token = jwtFilter.getJwtFromRequest(request);
////        System.out.println("token: " + token);
////        Long userId = 0L;
////        if (token != null) {
////            userId = jwtService.extractUserId(token);
////            System.out.println("UserEntity Id: " + userId);
////            if (userId == null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
////        }
////        Long use
//        Long userId = (Long) request.getAttribute("userId");
//
//        return ResponseEntity.ok(forumService.getForumData(userId));
//    }

    @GetMapping("/search/{key}")
    public ResponseEntity<?> searchPost(@PathVariable String key, HttpServletRequest request) {
        System.out.println("Search key: " + key);
        Long userId = (Long) request.getAttribute("userId");

        List<ShortForumPostResponse> posts = forumPostService.getForumPostByKeyword(key, userId);
        return ResponseEntity.ok(posts);
    }

    @PostMapping("/add-post")
    public ResponseEntity<?> addPost(@RequestBody ForumPostRequest postData, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        System.out.println("UserEntity id in add-forumPostEntity: " + userId);
        postData.setUserId(userId);

        System.out.println("Content of new post: " + postData.getContent());
        System.out.println("Title of new post: " + postData.getTitle());
        System.out.println("Categories of new post: " + postData.getSelectedCategoryList());
        ForumPostResponse post = forumPostService.addForumPost(postData);
//        ForumPostResponse post = null;
        return ResponseEntity.ok(post);
    }

    @GetMapping("/post-detail/{postId}")
    public ResponseEntity<?> getForumDetail(@PathVariable Long postId, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        System.out.println("UserEntity id in get forumdetail: " + userId);
        ForumPostDetailResponse response = ForumPostDetailResponse.builder()
                .post(forumPostService.getForumPostById(postId, userId, request))
                .comments(forumPostCommentService.getPostCommentsByPostId(postId))
                .build();

        return ResponseEntity.ok(response);
    }

//    @GetMapping("/post-list")
//    public ResponseEntity<?> getPostList(HttpServletRequest request) {
//        Long userId = (Long) request.getAttribute("userId");
//        List<ShortForumPostResponse> posts = forumPostService.getForumPostListReverse(userId);
//        return ResponseEntity.ok(posts);
//    }

    @PatchMapping("/save-post")
    public ResponseEntity<String> saveForumPosts(@RequestBody List<SaveStatusUpdateRequest> requests, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        if (userId != 0L) {
            forumPostSaveService.updateSaveStatuses(requests, userId);
        }
        return ResponseEntity.ok("Save successful");
    }

//    @GetMapping("/post-list/type/{typeId}")
//    public ResponseEntity<?> getPostListByType(@PathVariable int typeId, HttpServletRequest request) {
//        Long userId = (Long) request.getAttribute("userId");
//        if (userId != 0L) {
//            return ResponseEntity.ok(forumPostService.getPostListByType(typeId, userId));
//        }
//        return ResponseEntity.ok(getPostList(request));
//    }

    @PatchMapping("/save-post-detail/{postId}")
    public ResponseEntity<String> saveForumPostDetail(@PathVariable Long postId, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        System.out.println("Save forumPostEntity detail: " + userId);
        if (!userId.equals(0L)) {
            forumPostSaveService.updateDetailSaveStatus(postId, userId);
        }
        return ResponseEntity.ok("Save successful");
    }

    @PostMapping("/add-cm")
    public ResponseEntity<?> addCm(@RequestBody ForumPostCommentRequest forumPostCommentRequest, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        System.out.println("UserEntity id in add-cm: " + forumPostCommentRequest.getContent());
        if (!userId.equals(0L)) {
            forumPostCommentRequest.setUserId(userId);
            ForumPostCommentResponse cm = forumPostCommentService.addPostComment(forumPostCommentRequest);
            System.out.println("Cm response is : " + cm);
            return ResponseEntity.ok(cm);
        }
        return ResponseEntity.ok("ForumPostEntity comment successful");
    }

    @PatchMapping("/like/{statusId}/{postId}")
    public ResponseEntity<String> likePost(@PathVariable int statusId, @PathVariable Long postId, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");

        forumPostLikeService.updateLikeStatuses(userId, postId, statusId);
        return ResponseEntity.ok("Change like status successful");
    }


    @GetMapping("")
    public ResponseEntity<?> getForumData(HttpServletRequest request,
                                          @RequestParam(defaultValue = "0") int page,
                                          @RequestParam(defaultValue = "false") boolean includePopular,
                                          @RequestParam(defaultValue = "false") boolean includeHistory
    ) {
        Long userId = (Long) request.getAttribute("userId");
        //Long userId = 0L;
        System.out.println("UserEntity id in getForumData: " + userId);

        return ResponseEntity.ok(forumService.getForumData(userId, includePopular, includeHistory, page));
    }

//    @GetMapping("/post-list")
//    public ResponseEntity<?> getPostList(HttpServletRequest request, @RequestParam(defaultValue = "0") int page) {
//        Long userId = (Long) request.getAttribute("userId");
//        List<ShortForumPostResponse> posts = forumPostService.getForumPostListByPage(userId, page);
//        return ResponseEntity.ok(posts);
//    }

    @GetMapping("/post-list")
    public ResponseEntity<?> getPostList(HttpServletRequest request, int page) {
        Long userId = (Long) request.getAttribute("userId");
        Page<ShortForumPostResponse> posts = forumPostService.getForumPostListByPage(userId, page);
        return ResponseEntity.ok(posts);
    }

    @GetMapping("/post-list/type/{typeId}")
    public ResponseEntity<?> getPostListByType(@PathVariable int typeId, HttpServletRequest request, @RequestParam(defaultValue = "0") int page) {
        Long userId = (Long) request.getAttribute("userId");
        if (userId != 0L) {
            return ResponseEntity.ok(forumPostService.getPostListByType(typeId, userId, page));
        }
        return ResponseEntity.ok(getPostList(request, page));
    }

    @PatchMapping("/update-post/{postId}")
    public ResponseEntity<String> likePost(@PathVariable Long postId, @RequestBody ForumPostUpdateRequest forumPostRequest) {
//        Long userId = (Long) request.getAttribute("userId");

        forumPostService.updateForumPost(postId, forumPostRequest.getTitle(), forumPostRequest.getContent(), forumPostRequest.getStatus());
        return ResponseEntity.ok("Change like status successful");
    }
}
