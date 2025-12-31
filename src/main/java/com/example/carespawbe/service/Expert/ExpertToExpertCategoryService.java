package com.example.carespawbe.service.Expert;

import com.example.carespawbe.entity.Expert.ExpertCategoryEntity;
import com.example.carespawbe.entity.Expert.ExpertEntity;
import com.example.carespawbe.entity.Expert.ExpertToExpertCategoryEntity;
import com.example.carespawbe.repository.Expert.ExpertToExpertCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExpertToExpertCategoryService {

    @Autowired
    private ExpertToExpertCategoryRepository expertToExpertCategoryRepository;

    @Autowired
    private ExpertCategoryService categoryEntityService;

    public boolean addExpertToCategory(ExpertEntity expertEntity, Long categoryId) {
        ExpertCategoryEntity expertCategory = categoryEntityService.getById(categoryId);
        if (expertCategory == null) {
            return false;
        }
        try {
            ExpertToExpertCategoryEntity entity = new ExpertToExpertCategoryEntity(expertEntity, expertCategory);
            ExpertToExpertCategoryEntity saveEntity = expertToExpertCategoryRepository.save(entity);
        } catch (Exception e) {
            System.out.println("Error in add Expert Category: " + e.getMessage());
            return false;
        }
        return true;
    }

}
