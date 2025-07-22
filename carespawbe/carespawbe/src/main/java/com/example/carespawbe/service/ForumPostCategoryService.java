package com.example.carespawbe.service;

import com.example.carespawbe.dto.Forum.ForumPostCategoryRequest;
import com.example.carespawbe.entity.ForumPostToCategoryEntity;
import com.example.carespawbe.mapper.ForumPostCategoryMapper;
import com.example.carespawbe.repository.ForumPostToCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ForumPostCategoryService {

    @Autowired
    private ForumPostToCategoryRepository forumPostToCategoryRepository;

    @Autowired
    private ForumPostCategoryMapper forumPostCategoryMapper;

    public void addCategoryList(List<ForumPostCategoryRequest> forumPostCategoryRequestList) {
        List<ForumPostToCategoryEntity> postCategoryList = forumPostCategoryMapper.toPocategoryList(forumPostCategoryRequestList);
        System.out.println("List of ForumPostEntity Category after change: " + postCategoryList);
        forumPostToCategoryRepository.saveAll(postCategoryList);
    }
}
