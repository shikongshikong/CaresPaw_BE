package com.example.carespawbe.controller;

import com.example.carespawbe.dto.Forum.ForumPostRequest;
import com.example.carespawbe.dto.Forum.ShortForumPost;
import com.example.carespawbe.entity.ForumPost;
import com.example.carespawbe.service.ForumPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/")
@CrossOrigin(origins = "*")
public class ForumPostController {

    @Autowired
    private ForumPostService forumPostService;

    @PostMapping("")
    public ResponseEntity<?> displayForum(@RequestBody Map<String, String> request) {
        System.out.println("User Id: " + request.get("userId"));
        String userId = request.get("userId");
        List<ShortForumPost> posts = forumPostService.searchForumPost(userId);

//      popular post

//      list post reverse
//      history
//      save

        return ResponseEntity.ok(posts);
    }

    @PostMapping("/search")
    public ResponseEntity<?> searchPost(@RequestBody Map<String, String> request) {
        System.out.println("Search key: " + request.get("keyword"));
        String keyword = request.get("keyword");
        List<ShortForumPost> posts = forumPostService.searchForumPost(keyword);
        return ResponseEntity.ok(posts);
    }

    @PostMapping("/add")
    public ResponseEntity<?> addPost(@RequestBody ForumPostRequest  forumPostRequest) {
        ForumPost post = forumPostService.addForumPost(forumPostRequest);
        if (post != null) {
            return ResponseEntity.ok(post);
        }
        return ResponseEntity.notFound().build();
    }

}
