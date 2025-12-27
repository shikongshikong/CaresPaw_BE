package com.example.carespawbe.service.Forum;

import com.example.carespawbe.entity.Forum.ForumPostTypeEntity;
import com.example.carespawbe.repository.Forum.ForumPostRepository;
import com.example.carespawbe.repository.Forum.ForumPostTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ForumPostTypeService {

    @Autowired
    private ForumPostTypeRepository forumPostTypeRepository;

    public ForumPostTypeEntity getForumPostTypeById(int id){
        return forumPostTypeRepository.findById((long) id).orElse(null);
    }
}
