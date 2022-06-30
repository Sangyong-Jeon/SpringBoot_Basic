package com.example.springboot_basic.repository;

import com.example.springboot_basic.domain.post.Post;
import com.example.springboot_basic.dto.post.PostSearch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    // 동적으로 쿼리를 넣을 수 없어서 상황마다 함수를 만들어줬음!!!
    // 게시글 전체 조회 - 제목으로 검색
    @Query("select p from Post p where p.title Like %:#{#search.postTitle}%")
    List<Post> findPostsByTitle(@Param("search") PostSearch postSearch);

    // 게시글 전체 조회 - 카테고리로 검색
    @Query("select p from Post p where p.category =:#{#search.postCategory}")
    List<Post> findPostsByCategory(@Param("search") PostSearch postSearch);

    // 게시글 전체 조회 - 제목과 카테고리로 검색
    @Query("select p from Post p where p.title Like %:#{#search.postTitle}% and p.category =:#{#search.postCategory}")
    List<Post> findPosts(@Param("search") PostSearch postSearch);
}
