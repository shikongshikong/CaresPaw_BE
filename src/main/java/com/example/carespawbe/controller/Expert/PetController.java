package com.example.carespawbe.controller.Expert;

import com.example.carespawbe.dto.Expert.qanda.PetResponse;
import com.example.carespawbe.dto.Expert.qanda.SpeciesResponse;
import com.example.carespawbe.service.Expert.booking.PetService;
import com.example.carespawbe.service.Forum.ForumPostTypeService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/pet")
@RequiredArgsConstructor
public class PetController {

    private final ForumPostTypeService typeService;
    private final PetService  petService;

    @GetMapping("/species")
    public List<SpeciesResponse> getSpecies() {
        return typeService.getSpecies();
    }

    @GetMapping("/my-pet")
    public List<PetResponse> getMyPets(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        if (userId == null) userId = (Long) request.getSession().getAttribute("userId");

        if (userId == null) {
            throw new RuntimeException("Unauthenticated");
        }

        return petService.getMyPets(userId);
    }
}
