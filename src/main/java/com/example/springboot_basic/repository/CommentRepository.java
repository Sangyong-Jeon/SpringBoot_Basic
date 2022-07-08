package com.example.springboot_basic.repository;

import com.example.springboot_basic.domain.comment.Comment;
import com.example.springboot_basic.domain.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {


    @Query("select c from Comment c where c.parent is null")
    List<Comment> findCommentsForPost(Post post);
}
