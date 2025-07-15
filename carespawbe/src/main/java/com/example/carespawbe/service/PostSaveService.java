package com.example.carespawbe.service;

import com.example.carespawbe.dto.History.PostSideBarResponse;
import com.example.carespawbe.dto.Save.SaveStatusUpdateRequest;
import com.example.carespawbe.entity.ForumPost;
import com.example.carespawbe.entity.ForumPostSave;
import com.example.carespawbe.entity.User;
import com.example.carespawbe.mapper.PostSaveMapper;
import com.example.carespawbe.repository.ForumPostRepository;
import com.example.carespawbe.repository.PostSaveRepository;
import com.example.carespawbe.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostSaveService {

    @Autowired
    private PostSaveRepository postSaveRepository;

    @Autowired
    private ForumPostRepository forumPostRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostSaveMapper postSaveMapper;

//    @Autowired
//    private PostS

//    public void addPostSave()
    public List<PostSideBarResponse> get5SavedByUserId(Long userId) {
        Pageable pageable = PageRequest.of(0, 5, Sort.by("savedAt").descending());
        List<ForumPostSave> saves = postSaveRepository.findForumPostSavesByUserId(userId, pageable);
        if (saves.isEmpty()) {
            return null;
        }
        List<PostSideBarResponse> saveResponses = postSaveMapper.toSaveResponseList(saves);
        for (PostSideBarResponse save : saveResponses) {
            save.setSaved(true);
        }
        return saveResponses;
    }

    public void updateSaveStatuses(List<SaveStatusUpdateRequest> requests, Long userId) {
        for (SaveStatusUpdateRequest request : requests) {
            Long postId = request.getPostId();
            boolean isSaved = postSaveRepository.existsByUserIdAndPostId(userId, postId);

            if (isSaved && request.getIsSaved() == false) {
                postSaveRepository.deleteByUserIdAndPostId(userId, postId);
            }
            if (request.getIsSaved() == true) {
                User user = userRepository.findById(userId).get();
                ForumPost post = forumPostRepository.findForumPostById(postId).get();
                ForumPostSave forumPostSave = ForumPostSave.builder()
                        .user(user)
                        .post(post)
                        .build();
                postSaveRepository.save(forumPostSave);
            }
        }
    }

    public void updateDetailSaveStatus(Long postId, Long userId) {
            boolean isSaved = postSaveRepository.existsByUserIdAndPostId(userId, postId);

            if (isSaved) {
                postSaveRepository.deleteByUserIdAndPostId(userId, postId);
            }
            else {
                User user = userRepository.findById(userId).get();
                ForumPost post = forumPostRepository.findForumPostById(postId).get();
                ForumPostSave forumPostSave = ForumPostSave.builder()
                        .user(user)
                        .post(post)
                        .build();
                postSaveRepository.save(forumPostSave);
            }
    }
}
