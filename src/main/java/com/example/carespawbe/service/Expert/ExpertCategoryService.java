package com.example.carespawbe.service.Expert;

import com.example.carespawbe.entity.Expert.ExpertCategoryEntity;
import com.example.carespawbe.repository.Expert.ExpertCategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ExpertCategoryService {

    @Autowired
    private ExpertCategoryRepository expertCategoryRepository;

    public ExpertCategoryEntity getById(Long categoryId) {
        return expertCategoryRepository.findById(categoryId).orElse(null);
    }
}
