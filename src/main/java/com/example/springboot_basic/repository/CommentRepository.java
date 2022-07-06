package com.example.springboot_basic.repository;

import com.example.springboot_basic.domain.comment.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
}
