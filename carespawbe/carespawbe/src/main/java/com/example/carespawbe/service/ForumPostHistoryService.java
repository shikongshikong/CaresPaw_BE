package com.example.carespawbe.service;

import com.example.carespawbe.dto.Forum.ForumPostDetailRequest;
import com.example.carespawbe.dto.History.ForumPostHistoryTagResponse;
import com.example.carespawbe.dto.History.ForumPostSideBarResponse;
import com.example.carespawbe.dto.UserProfile.UserHistoryResponse;
import com.example.carespawbe.entity.ForumPostHistoryEntity;
import com.example.carespawbe.mapper.ForumPostHistoryMapper;
import com.example.carespawbe.repository.ForumPostHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ForumPostHistoryService {

    @Autowired
    private ForumPostHistoryRepository forumPostHistoryRepository;

    @Autowired
    private ForumPostHistoryMapper forumPostHistoryMapper;

    public void addPostHistory(ForumPostDetailRequest request) {
        ForumPostHistoryEntity history = forumPostHistoryMapper.toHistoryEntity(request);
        forumPostHistoryRepository.save(history);
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

}
