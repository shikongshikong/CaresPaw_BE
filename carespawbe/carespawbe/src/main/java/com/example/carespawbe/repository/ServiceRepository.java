package com.example.carespawbe.repository;

import com.example.carespawbe.entity.Service;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServiceRepository extends JpaRepository<Service, Long> {
    List<Service> findByName(String name);

    Service findServiceById(Long id);

    List<Service> findAllByUserId(Long userId);
}
