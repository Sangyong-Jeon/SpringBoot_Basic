package com.example.springboot_basic.repository;

import com.example.springboot_basic.domain.post.File;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<File, Long> {
}
