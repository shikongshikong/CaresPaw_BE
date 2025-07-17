package com.example.carespawbe.service;

import com.example.carespawbe.dto.Forum.PostCategoryRequest;
import com.example.carespawbe.entity.PostBelongToCategory;
import com.example.carespawbe.mapper.PostCategoryMapper;
import com.example.carespawbe.repository.PostBelongToCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostCategoryService {

    @Autowired
    private PostBelongToCategoryRepository  postBelongToCategoryRepository;

    @Autowired
    private PostCategoryMapper postCategoryMapper;

    public void addCategoryList(List<PostCategoryRequest> postCategoryRequestList) {
        List<PostBelongToCategory> postCategoryList = postCategoryMapper.toPocategoryList(postCategoryRequestList);
        System.out.println("List of Post Category after change: " + postCategoryList);
        postBelongToCategoryRepository.saveAll(postCategoryList);
    }
}
