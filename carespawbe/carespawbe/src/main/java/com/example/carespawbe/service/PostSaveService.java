package com.example.carespawbe.service;

import com.example.carespawbe.dto.History.PostSideBarResponse;
import com.example.carespawbe.entity.ForumPostSave;
import com.example.carespawbe.mapper.PostSaveMapper;
import com.example.carespawbe.repository.PostSaveRepository;
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
        return postSaveMapper.toSaveResponseList(saves);
    }
}
