package com.example.springboot_basic.repository;

import com.example.springboot_basic.domain.member.Member;
import com.example.springboot_basic.domain.post.Post;
import com.example.springboot_basic.dto.post.PostSearch;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    // 게시글 전체 조회 - 페이지네이션
    @Query(value = "select p from Post p",
            countQuery = "select count(p) from Post p")
    Page<Post> findAllPaging(Pageable pageable);

    // 게시글 전체 조회 - 제목으로 검색
    @Query(value = "select p from Post p where p.title Like %:#{#search.getPostTitle()}%",
            countQuery = "select count(p) from Post p where p.title Like %:#{#search.getPostTitle()}%")
    Page<Post> findPostsByTitle(@Param("search") PostSearch postSearch, Pageable pageable);

    // 게시글 전체 조회 - 카테고리로 검색
    @Query(value = "select p from Post p where p.category =:#{#search.getPostCategory()}",
            countQuery = "select count(p) from Post p where p.category =:#{#search.getPostCategory()}")
    Page<Post> findPostsByCategory(@Param("search") PostSearch postSearch, Pageable pageable);

    // 게시글 전체 조회 - 제목과 카테고리로 검색
    @Query(value = "select p from Post p where p.title Like %:#{#search.getPostTitle()}% and p.category =:#{#search.getPostCategory()}",
            countQuery = "select count(p) from Post p where p.title Like %:#{#search.getPostTitle()}% and p.category =:#{#search.getPostCategory()}")
    Page<Post> findPosts(@Param("search") PostSearch postSearch, Pageable pageable);

    List<Post> findByMember(Member member);
}