package com.example.springboot_basic.repository;

import com.example.springboot_basic.domain.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {
}
