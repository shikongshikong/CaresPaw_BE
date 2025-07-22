package com.example.carespawbe.controller;

import com.example.carespawbe.dto.Forum.ForumPostRequest;
import com.example.carespawbe.dto.UserProfile.UserProfileData;
import com.example.carespawbe.service.UserProfileService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/profile")
@CrossOrigin(origins = "http://localhost:3000")
public class UserProfileController {

    @Autowired
    private UserProfileService userProfileService;

    @GetMapping("")
    public ResponseEntity<?> getUserProfileData(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        UserProfileData data = userProfileService.getUserProfileData(userId);
        System.out.println("User Profile Data: " + data.getUser().getEmail());
        return ResponseEntity.ok(data);
    }

    @PatchMapping("/update-post")
    public ResponseEntity<?> updatePost(@RequestBody ForumPostRequest forumPostRequest, Long postId, HttpServletRequest request) {
        int updateRow = userProfileService.updateUserPost(postId, forumPostRequest);
        if (postId == null || updateRow == 0) {
            return ResponseEntity.ok("Update ForumPostEntity Failed!");
        }
        return ResponseEntity.ok("Update ForumPostEntity Success!");
    }

    @DeleteMapping("/delete-post/{postId}")
    public ResponseEntity<?> deletePost(@PathVariable Long postId, HttpServletRequest request) {
        int deleteRow = userProfileService.deleteUserPost(postId);
        if (deleteRow == 0) {
            return ResponseEntity.ok("Delete ForumPostEntity Failed!");
        }
        return ResponseEntity.ok("Delete ForumPostEntity Success!");
    }
}
