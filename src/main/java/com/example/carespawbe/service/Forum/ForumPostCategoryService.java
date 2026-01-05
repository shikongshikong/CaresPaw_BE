package com.example.carespawbe.service.Forum;

import com.example.carespawbe.dto.Forum.ForumPostCategoryRequest;
import com.example.carespawbe.entity.Forum.ForumPostToCategoryEntity;
import com.example.carespawbe.mapper.Forum.ForumPostCategoryMapper;
import com.example.carespawbe.repository.Forum.ForumPostToCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ForumPostCategoryService {

    @Autowired
    private ForumPostToCategoryRepository forumPostToCategoryRepository;

//    @Autowired
//    private ForumPostCa forumPostToCategoryRepository;

    @Autowired
    private ForumPostCategoryMapper forumPostCategoryMapper;

    public void addCategoryList(List<ForumPostCategoryRequest> forumPostCategoryRequestList) {
        List<ForumPostToCategoryEntity> postCategoryList = forumPostCategoryMapper.toPostcategoryList(forumPostCategoryRequestList);
        for (ForumPostToCategoryEntity post : postCategoryList) {
            System.out.println("CategoryId of post: " +  post.getForumPostCategoryEntity().getId());
            forumPostToCategoryRepository.save(post);
        }
        System.out.println("List of ForumPostEntity Category after change: " + postCategoryList);
//        forumPostToCategoryRepository.saveAll(postCategoryList);
    }

    public List<Integer> getCategoryListByForumPostId(Long forumPostId) {
        List<ForumPostToCategoryEntity> forumPostToCategoryEntities = forumPostToCategoryRepository.findAllByForumPostEntityId(forumPostId);

        List<Integer> categoryIds =  new ArrayList<>();
        for (ForumPostToCategoryEntity post : forumPostToCategoryEntities) {
            categoryIds.add(post.getForumPostCategoryEntity().getId());
        }

        return categoryIds;
    }
}
