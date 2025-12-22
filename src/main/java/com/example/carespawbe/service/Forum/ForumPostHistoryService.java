package com.example.carespawbe.service.Forum;

import com.example.carespawbe.dto.Forum.ForumPostDetailRequest;
import com.example.carespawbe.dto.History.ForumHistoryTrainDTO;
import com.example.carespawbe.dto.History.ForumPostHistoryTagResponse;
import com.example.carespawbe.dto.UserProfile.UserHistoryResponse;
import com.example.carespawbe.entity.Forum.ForumPostHistoryEntity;
import com.example.carespawbe.mapper.Forum.ForumPostHistoryMapper;
import com.example.carespawbe.repository.Forum.ForumPostHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class ForumPostHistoryService {

    @Autowired
    private ForumPostHistoryRepository forumPostHistoryRepository;

    @Autowired
    private ForumPostHistoryMapper forumPostHistoryMapper;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private void saveToRedis(ForumPostHistoryEntity history) {

        String key = "user:" + history.getUser().getId() + ":history";
        long now = System.currentTimeMillis();

        redisTemplate.opsForZSet()
                .add(key, history.getForumPostEntity().getId().toString(), now);

        redisTemplate.opsForZSet()
                .removeRange(key, 0, -101);

        redisTemplate.expire(key, 7, TimeUnit.DAYS);
    }


    public void addPostHistory(ForumPostDetailRequest request) {
        ForumPostHistoryEntity history = forumPostHistoryMapper.toHistoryEntity(request);

        forumPostHistoryRepository.save(history);

        try {
            saveToRedis(history);
        } catch (Exception e) {
            System.out.println("Redis save failed" + e.getMessage());
        }
    }

    public boolean isExistHistoryByUserIdAndPostId(Long userId, Long postId) {
        LocalDate currentDate = LocalDate.now();
        List<ForumPostHistoryEntity> history = forumPostHistoryRepository.findByUserIdAndForumPostEntityIdAndCreatedAt(userId, postId, currentDate);
        return !history.isEmpty();
    }

    public List<ForumPostHistoryTagResponse> get5PostHistoryByUserId(Long userId) {
        Pageable pageable = PageRequest.of(0, 5);
//        List<ForumPostHistoryEntity> histories = forumPostHistoryRepository.findForumPostHistoryEntitiesByUserId(userId, pageable);
//        if (histories.size() > 0) {
//            return forumPostHistoryMapper.toHistoryResponseList(histories);
//        }
        List<ForumPostHistoryTagResponse> histories = forumPostHistoryRepository.findForumPostHistoryEntityByUserIdHasFollow(userId, pageable);
        if (histories != null) return histories;
        return null;
    }

    public List<UserHistoryResponse> getUserHistoryByUserId(Long userId) {
        return forumPostHistoryRepository.findHistoryEntityByUserId(userId);
    }

    public List<ForumHistoryTrainDTO> getAllHistory() {
        List<ForumPostHistoryEntity> historyEntities = forumPostHistoryRepository.findAll();
        List<ForumHistoryTrainDTO> forumHistoryTrainDTOs = historyEntities.stream()
                .map(h -> new ForumHistoryTrainDTO(
                        h.getUser().getId(),
                        h.getId(),
                        h.getCreatedAt()
                )).toList();
        return forumHistoryTrainDTOs;
    }

//    public void savePostForTrain(Long userId, Long postId) {
//        String key = "user:" + userId + "history";
//        long now = System.currentTimeMillis();
//    }

}
