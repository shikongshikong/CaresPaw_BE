package com.example.carespawbe.service.Forum;

import com.example.carespawbe.dto.Expert.qanda.SpeciesResponse;
import com.example.carespawbe.entity.Forum.ForumPostTypeEntity;
import com.example.carespawbe.repository.Forum.ForumPostRepository;
import com.example.carespawbe.repository.Forum.ForumPostTypeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ForumPostTypeService {

    @Autowired
    private ForumPostTypeRepository forumPostTypeRepository;

    public ForumPostTypeEntity getForumPostTypeById(int id){
        return forumPostTypeRepository.findById((long) id).orElse(null);
    }

    public List<SpeciesResponse> getSpecies() {
        List<ForumPostTypeEntity> list = forumPostTypeRepository.findAll();

        // sort theo name cho đẹp (optional)
        list.sort((a, b) -> {
            String an = a.getName() == null ? "" : a.getName();
            String bn = b.getName() == null ? "" : b.getName();
            return an.compareToIgnoreCase(bn);
        });

        return list.stream()
                .map(c -> new SpeciesResponse(c.getId(), c.getName(), c.getImage()))
                .toList();
    }
}
