package com.example.carespawbe.service.Expert;

import com.example.carespawbe.dto.Expert.ExpertApplyRequest;
import com.example.carespawbe.entity.Expert.ExpertEntity;
import com.example.carespawbe.mapper.Expert.ExpertEntityMapper;
import com.example.carespawbe.repository.Expert.ExpertRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ExpertEntityService {

    @Autowired
    ExpertEntityMapper expertEntityMapper;

    @Autowired
    ExpertRepository  expertRepository;

    public ExpertEntity addExpert(ExpertApplyRequest applyRequest) {
        ExpertEntity expertEntity = expertEntityMapper.toExpertEntity(applyRequest);
        // just save file name
        expertEntity.setIdImage(applyRequest.getIdImage().getOriginalFilename());

        try {
            return expertRepository.save(expertEntity);
        }  catch (Exception e) {
            System.out.println("Save Entity Error!: " + e.getMessage());
            return null;
        }
    }
}
