package com.example.carespawbe.service;

import com.example.carespawbe.dto.History.ForumPostSideBarResponse;
import com.example.carespawbe.dto.Save.SaveStatusUpdateRequest;
import com.example.carespawbe.entity.ForumPostEntity;
import com.example.carespawbe.entity.ForumPostSaveEntity;
import com.example.carespawbe.entity.UserEntity;
import com.example.carespawbe.mapper.ForumPostSaveMapper;
import com.example.carespawbe.repository.ForumPostRepository;
import com.example.carespawbe.repository.ForumPostSaveRepository;
import com.example.carespawbe.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ForumPostSaveService {

    @Autowired
    private ForumPostSaveRepository forumPostSaveRepository;

    @Autowired
    private ForumPostRepository forumPostRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ForumPostSaveMapper forumPostSaveMapper;

//    @Autowired
//    private PostS

//    public void addPostSave()
//    public List<ForumPostSideBarResponse> get5SavedByUserId(Long userId) {
//        Pageable pageable = PageRequest.of(0, 5, Sort.by("savedAt").descending());
//        List<ForumPostSaveEntity> saves = forumPostSaveRepository.findForumPostSavesByUserId(userId, pageable);
//        if (saves.isEmpty()) {
//            return null;
//        }
//        List<ForumPostSideBarResponse> saveResponses = forumPostSaveMapper.toSaveResponseList(saves);
//        for (ForumPostSideBarResponse save : saveResponses) {
//            save.setSaved(true);
//        }
//        return saveResponses;
//    }

    public void updateSaveStatuses(List<SaveStatusUpdateRequest> requests, Long userId) {
        for (SaveStatusUpdateRequest request : requests) {
            Long postId = request.getPostId();
            boolean isSaved = forumPostSaveRepository.existsByUserIdAndForumPostEntityId(userId, postId);

            if (isSaved && request.getIsSaved() == false) {
                forumPostSaveRepository.deleteByUserIdAndForumPostEntityId(userId, postId);
            }
            if (request.getIsSaved() == true) {
                UserEntity userEntity = userRepository.findById(userId).get();
                ForumPostEntity forumPostEntity = forumPostRepository.findForumPostById(postId).get();
                ForumPostSaveEntity forumPostSaveEntity = ForumPostSaveEntity.builder()
                        .user(userEntity)
                        .forumPostEntity(forumPostEntity)
                        .build();
                forumPostSaveRepository.save(forumPostSaveEntity);
            }
        }
    }

    @Transactional
    public void updateDetailSaveStatus(Long postId, Long userId) {
            boolean isSaved = forumPostSaveRepository.existsByUserIdAndForumPostEntityId(userId, postId);

            if (isSaved) {
                forumPostSaveRepository.deleteByUserIdAndForumPostEntityId(userId, postId);
            }
            else {
                UserEntity userEntity = userRepository.findById(userId).get();
                ForumPostEntity forumPostEntity = forumPostRepository.findForumPostById(postId).get();
                ForumPostSaveEntity forumPostSaveEntity = ForumPostSaveEntity.builder()
                        .user(userEntity)
                        .forumPostEntity(forumPostEntity)
                        .build();
                forumPostSaveRepository.save(forumPostSaveEntity);
            }
    }
}
