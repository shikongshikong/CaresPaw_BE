package com.example.carespawbe.service;

import com.example.carespawbe.dto.History.PostSideBarResponse;
import com.example.carespawbe.dto.Save.SaveStatusUpdateRequest;
import com.example.carespawbe.entity.Post;
import com.example.carespawbe.entity.PostSave;
import com.example.carespawbe.entity.User;
import com.example.carespawbe.mapper.PostSaveMapper;
import com.example.carespawbe.repository.PostRepository;
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
    private PostRepository postRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PostSaveMapper postSaveMapper;

//    @Autowired
//    private PostS

//    public void addPostSave()
    public List<PostSideBarResponse> get5SavedByUserId(Long userId) {
        Pageable pageable = PageRequest.of(0, 5, Sort.by("savedAt").descending());
        List<PostSave> saves = postSaveRepository.findForumPostSavesByUserId(userId, pageable);
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
                Post post = postRepository.findForumPostById(postId).get();
                PostSave postSave = PostSave.builder()
                        .user(user)
                        .post(post)
                        .build();
                postSaveRepository.save(postSave);
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
                Post post = postRepository.findForumPostById(postId).get();
                PostSave postSave = PostSave.builder()
                        .user(user)
                        .post(post)
                        .build();
                postSaveRepository.save(postSave);
            }
    }
}
