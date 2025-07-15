package com.example.carespawbe.controller;

import com.example.carespawbe.dto.Forum.*;
import com.example.carespawbe.dto.Save.SaveStatusUpdateRequest;
import com.example.carespawbe.entity.ForumPost;
import com.example.carespawbe.security.JwtAuthenticationFilter;
import com.example.carespawbe.security.JwtService;
import com.example.carespawbe.service.ForumPostService;
import com.example.carespawbe.service.ForumService;
import com.example.carespawbe.service.PostCommentService;
import com.example.carespawbe.service.PostSaveService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/forum")
@CrossOrigin(origins = "http://localhost:3000")
public class ForumPostController {

    @Autowired
    private ForumPostService forumPostService;

    @Autowired
    private ForumService forumService;

    @Autowired
    private PostSaveService postSaveService;

    @Autowired
    private PostCommentService postCommentService;

    @GetMapping("")
    public ResponseEntity<?> getForumData(HttpServletRequest request) {
//        String token = jwtFilter.getJwtFromRequest(request);
//        System.out.println("token: " + token);
//        Long userId = 0L;
//        if (token != null) {
//            userId = jwtService.extractUserId(token);
//            System.out.println("User Id: " + userId);
//            if (userId == null) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
//        }
//        Long use
        Long userId = (Long) request.getAttribute("userId");

        return ResponseEntity.ok(forumService.getForumData(userId));
    }

    @GetMapping("/search/{key}")
    public ResponseEntity<?> searchPost(@PathVariable String key, HttpServletRequest request) {
        System.out.println("Search key: " + key);
        Long userId = (Long) request.getAttribute("userId");

        List<ShortForumPost> posts = forumPostService.getForumPostByKeyword(key, userId);
        return ResponseEntity.ok(posts);
    }

    @PostMapping("/add-post")
    public ResponseEntity<?> addPost(@RequestBody ForumPostRequest forumPostRequest, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        System.out.println("User id in add-post: " + userId);
        forumPostRequest.setUserId(userId);
        PostResponse post = forumPostService.addForumPost(forumPostRequest);
        return ResponseEntity.ok(post);
    }

    @GetMapping("/post-detail/{postId}")
    public ResponseEntity<?> getForumDetail(@PathVariable Long postId, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        System.out.println("User id in post detail: " + userId);
        PostDetailResponse response = PostDetailResponse.builder()
                .post(forumPostService.getForumPostById(postId, userId, request))
                .comments(postCommentService.getPostCommentsByPostId(postId))
                .build();
//        PostResponse post = forumPostService.getForumPostById(postId, userId, request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/post-list")
    public ResponseEntity<?> getPostList(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        List<ShortForumPost> posts = forumPostService.getForumPostListReverse(userId);
        return ResponseEntity.ok(posts);
    }

    @PatchMapping("/save-post")
    public ResponseEntity<String> saveForumPosts(@RequestBody List<SaveStatusUpdateRequest> requests, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        if (userId != 0L) {
            postSaveService.updateSaveStatuses(requests, userId);
        }
        return ResponseEntity.ok("Save successful");
    }

    @GetMapping("/post-list/type/{type}")
    public ResponseEntity<?> getPostListByType(@PathVariable String type, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        if (userId != 0L) {
            return ResponseEntity.ok(forumPostService.getPostListByType(type, userId));
        }
        return ResponseEntity.ok(getPostList(request));
    }

    @PatchMapping("/save-post-detail/{postId}")
    public ResponseEntity<String> saveForumPostDetail(@PathVariable Long postId, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        System.out.println("Save post detail: " + userId);
        if (!userId.equals(0L)) {
            postSaveService.updateDetailSaveStatus(postId, userId);
        }
        return ResponseEntity.ok("Save successful");
    }

}
