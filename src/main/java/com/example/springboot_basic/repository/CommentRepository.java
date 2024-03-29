package com.example.springboot_basic.repository;

import com.example.springboot_basic.domain.comment.Comment;
import com.example.springboot_basic.domain.member.Member;
import com.example.springboot_basic.domain.post.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {


    @Query("select c from Comment c where c.parent is null and c.post =:post")
    List<Comment> findCommentsForPost(@Param("post") Post post);

    List<Comment> findByMember(Member member);
}
