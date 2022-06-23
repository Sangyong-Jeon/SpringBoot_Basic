package com.example.springboot_basic.repository;

import com.example.springboot_basic.domain.post.File;
import com.example.springboot_basic.domain.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileRepository extends JpaRepository<File, Long> {
    
    List<File> findByPost(Post post);
}
