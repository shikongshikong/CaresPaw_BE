package com.example.carespawbe.controller.UserProfile;

import com.example.carespawbe.dto.UserProfile.UserProfileData;
import com.example.carespawbe.dto.UserProfile.UserUpdateRequest;
import com.example.carespawbe.service.Auth.UserProfileService;
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
        System.out.println("User Profile Data: " + data.getUser().getBirthday());
        return ResponseEntity.ok(data);
    }

//    @PatchMapping("/update-post")
//    public ResponseEntity<?> updatePost(@RequestBody ForumPostRequest forumPostRequest, Long postId, HttpServletRequest request) {
//        int updateRow = userProfileService.updateUserPost(postId, forumPostRequest);
//        if (postId == null || updateRow == 0) {
//            return ResponseEntity.ok("Update ForumPostEntity Failed!");
//        }
//        return ResponseEntity.ok("Update ForumPostEntity Success!");
//    }

    @DeleteMapping("/delete-post/{postId}")
    public ResponseEntity<?> deletePost(@PathVariable Long postId, HttpServletRequest request) {
        int deleteRow = userProfileService.deleteUserPost(postId);
        if (deleteRow == 0) {
            return ResponseEntity.ok("Delete ForumPostEntity Failed!");
        }
        return ResponseEntity.ok("Delete ForumPostEntity Success!");
    }

    @PostMapping("/check-login")
    public ResponseEntity<String> loginState(HttpServletRequest request) {
//        try {
        Long userId = (Long) request.getAttribute("userId");
        System.out.println("is Login....");
        if (userId != null) {
            System.out.println("Login-ok");
            return ResponseEntity.ok("Login-ok");
        }
        System.out.println("Login-not");
        return ResponseEntity.ok("Login-not");

//        } catch (Exception e) {
//            return ResponseEntity.ok("Login-not");
//        }
    }

    @PatchMapping("/update")
    public ResponseEntity<String> updateUserProfile(@RequestBody UserUpdateRequest updateData, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        userProfileService.updateUserProfileData(updateData, userId);
        return ResponseEntity.ok("success");
    }
}
