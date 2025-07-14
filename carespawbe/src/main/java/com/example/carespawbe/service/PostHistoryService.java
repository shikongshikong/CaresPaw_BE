package com.example.carespawbe.service;

import com.example.carespawbe.dto.Forum.PostDetailRequest;
import com.example.carespawbe.dto.History.PostSideBarResponse;
import com.example.carespawbe.entity.ForumPostHistory;
import com.example.carespawbe.mapper.PostHistoryMapper;
import com.example.carespawbe.repository.PostHistoryRepository;
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
public class PostHistoryService {

    @Autowired
    private PostHistoryRepository postHistoryRepository;

    @Autowired
    private PostHistoryMapper postHistoryMapper;

    public void addPostHistory(PostDetailRequest request) {
        ForumPostHistory history = postHistoryMapper.toHistoryEntity(request);
        postHistoryRepository.save(history);
    }

    public boolean isExistHistoryByUserIdAndPostId(Long userId, Long postId) {
        LocalDate currentDate = LocalDate.now();
        List<ForumPostHistory> history = postHistoryRepository.findByUserIdAndPostIdAndCreatedAt(userId, postId, currentDate);
        return !history.isEmpty();
    }

    public List<PostSideBarResponse> get5PostHistoryByUserId(Long userId) {
        Pageable pageable = PageRequest.of(0, 5, Sort.by("createdAt").descending());
        List<ForumPostHistory> histories = postHistoryRepository.findForumPostHistoriesByUserId(userId, pageable);
        System.out.println("History 1 id: " + histories.get(1).getPost().getId());
        return postHistoryMapper.toHistoryResponseList(histories);
    }

}
