package com.example.carespawbe.service.Expert.common;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.*;
import java.util.UUID;

@Service
public class FileStorageService {

    private final Path root = Paths.get("uploads/pets");

    public String storePetImage(MultipartFile file, Long userId) {
        try {
            if (file == null || file.isEmpty()) return null;

            Files.createDirectories(root.resolve(String.valueOf(userId)));
            String safeName = UUID.randomUUID() + "_" + file.getOriginalFilename();
            Path target = root.resolve(String.valueOf(userId)).resolve(safeName);

            Files.copy(file.getInputStream(), target, StandardCopyOption.REPLACE_EXISTING);

            // Nếu bạn có static mapping /cdn/... thì trả URL, còn demo trả path
            return "/uploads/pets/" + userId + "/" + safeName;
        } catch (Exception e) {
            throw new RuntimeException("Store pet image failed", e);
        }
    }
}

