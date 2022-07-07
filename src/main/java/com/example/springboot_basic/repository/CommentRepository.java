package com.example.springboot_basic.repository;

import com.example.springboot_basic.domain.comment.Comment;
import com.example.springboot_basic.domain.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    
    List<Comment> findByPost(Post post);
}
