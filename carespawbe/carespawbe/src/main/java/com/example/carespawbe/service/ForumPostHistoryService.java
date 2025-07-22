package com.example.carespawbe.service;

//import com.example.carespawbe.dto.Forum.ForumPostDetailRequest;
import com.example.carespawbe.dto.Forum.ForumPostDetailRequest;
import com.example.carespawbe.dto.History.ForumPostSideBarResponse;
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

    public List<ForumPostSideBarResponse> get5PostHistoryByUserId(Long userId) {
        Pageable pageable = PageRequest.of(0, 5, Sort.by("createdAt").descending());
//        List<ForumPostHistoryEntity> histories = forumPostHistoryRepository.findForumPostHistoriesByUserId(userId, pageable);
        List<ForumPostHistoryEntity> histories = forumPostHistoryRepository.findForumPostHistoryEntitiesByUserId(userId, pageable);
//        System.out.println("History 1 id: " + histories.get(1).getForumPostEntity().getId());
        if (histories.size() > 0) {
            return forumPostHistoryMapper.toHistoryResponseList(histories);
        }
//        System.out.println("In ServiceEntity ho");
        return null;
    }

}
