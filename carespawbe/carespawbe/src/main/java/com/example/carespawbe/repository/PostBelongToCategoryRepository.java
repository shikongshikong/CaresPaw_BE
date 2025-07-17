package com.example.carespawbe.repository;

import com.example.carespawbe.entity.PostBelongToCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostBelongToCategoryRepository extends JpaRepository<PostBelongToCategory, Long> {

}
