package com.example.carespawbe.controller;

import com.example.carespawbe.dto.Forum.*;
import com.example.carespawbe.entity.ForumPost;
import com.example.carespawbe.service.ForumPostService;
import com.example.carespawbe.service.ForumService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/forum")
@CrossOrigin(origins = "*")
public class ForumPostController {

    @Autowired
    private ForumPostService forumPostService;

    @Autowired
    private ForumService forumService;

    @PostMapping("")
    public ResponseEntity<?> getForumData(@RequestBody ForumRequest request) {
        System.out.println("User Id: " + request.getUserId());
//        Long userId = request.getUserId();

        return ResponseEntity.ok(forumService.getForumData(request.getUserId()));
    }

    @GetMapping("/search/{key}")
    public ResponseEntity<?> searchPost(@PathVariable String key) {
//        System.out.println("Search key: " + key);
        List<ShortForumPost> posts = forumPostService.getForumPostByKeyword(key);
        return ResponseEntity.ok(posts);
    }

    @PostMapping("/add-post")
    public ResponseEntity<?> addPost(@RequestBody ForumPostRequest  forumPostRequest) {
        PostResponse post = forumPostService.addForumPost(forumPostRequest);
        return ResponseEntity.ok(post);
    }

    @PostMapping("/post-detail")
    public ResponseEntity<?> getForumDetail(@RequestBody PostDetailRequest postDetailRequest, HttpServletRequest request) {
        PostResponse post = forumPostService.getForumPostById(postDetailRequest, request);
//        System.out.println(postDetailRequest.getPostId() + " : " + request.getRequestURI());
        return ResponseEntity.ok(post);
    }

    @GetMapping("/post-list")
    public ResponseEntity<?> getPostList() {
        List<ShortForumPost> posts = forumPostService.getForumPostListReverse();
        return ResponseEntity.ok(posts);
    }

//    @PostMapping("/save-post")
//    public ResponseEntity<?> saveForumPost(@RequestBody ForumPostRequest forumPostRequest) {
//
//    }

}
