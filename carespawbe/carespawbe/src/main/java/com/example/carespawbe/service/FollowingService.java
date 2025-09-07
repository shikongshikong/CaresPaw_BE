package com.example.carespawbe.service;

import com.example.carespawbe.dto.Follow.FollowingResponse;
import com.example.carespawbe.entity.FollowingEntity;
import com.example.carespawbe.mapper.FollowingMapper;
import com.example.carespawbe.repository.FollowingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class FollowingService {

    @Autowired
    private FollowingMapper followingMapper;

    @Autowired
    private FollowingRepository followingRepository;

    @Autowired
    private UserService userService;

    public void addFollowing(Long follower_id, Long followee_id) {
        try {
            FollowingEntity followingEntity = FollowingEntity.builder()
                    .follower(userService.getUserById(follower_id))
                    .followee(userService.getUserById(followee_id))
                    .build();
            followingRepository.save(followingEntity);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public List<FollowingResponse> getFollowingListByFollowerId(Long follower_id) {
        List<FollowingEntity> followingEntities = followingRepository.findAllByFollowerId(follower_id);
        return followingMapper.toFollowingResponseList(followingEntities);
    }

    public void unFollowing(Long follower_id, Long followee_id) {
        FollowingEntity followingEntity = null;
        try {
            followingEntity = followingRepository.findByFollowerIdAndFolloweeId(follower_id, followee_id);
            if (followingEntity != null) {
                followingRepository.deleteById(followingEntity.getId());
            }
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
