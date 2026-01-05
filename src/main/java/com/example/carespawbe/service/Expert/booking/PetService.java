package com.example.carespawbe.service.Expert.booking;

import com.example.carespawbe.dto.Expert.qanda.PetResponse;
import com.example.carespawbe.entity.Expert.PetEntity;
import com.example.carespawbe.repository.Expert.PetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PetService {

    private final PetRepository petRepository;

    public List<PetResponse> getMyPets(Long userId) {
        List<PetEntity> pets = petRepository.findMyPets(userId);

        return pets.stream()
                .map(p -> new PetResponse(
                        p.getId(),
                        p.getName(),
                        p.getBreed(),
                        p.getGender(),
                        p.getDateOfBirth(),
                        p.getWeight(),
                        p.getImageUrl(),
                        p.getSpecies() != null ? p.getSpecies().getId() : null,
                        p.getSpecies() != null ? p.getSpecies().getName() : null
                ))
                .toList();
    }
}

