package com.example.carespawbe.controller;

import com.example.carespawbe.service.FollowingService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/follow")
@CrossOrigin(origins = "http://localhost:3000")
public class FollowingController {

    @Autowired
    private FollowingService followingService;

    @PostMapping("/add")
    public ResponseEntity<String> addFollowing(@RequestBody Map<String, Long> request, HttpServletRequest httpServletRequest) {
        System.out.println("FolloweeId: " + request);
        Long followeeIdLong = request.get("followeeId");
        Long userid = (Long) httpServletRequest.getAttribute("userId");
        try {
            followingService.addFollowing(userid, followeeIdLong);
            System.out.println("Add FolloweeId: " +  followeeIdLong);
            return ResponseEntity.ok("success");
        }  catch (Exception e) {
            return ResponseEntity.ok("fail");
        }
    }

    @DeleteMapping("/remove/{followeeId}")
    public ResponseEntity<String> deleteFollowing(@PathVariable Long followeeId, HttpServletRequest httpServletRequest) {
        Long userid = (Long) httpServletRequest.getAttribute("userId");
        try {
            followingService.unFollowing(userid, followeeId);
            return ResponseEntity.ok("success");
        }  catch (Exception e) {
            return ResponseEntity.ok("fail");
        }
    }
}
