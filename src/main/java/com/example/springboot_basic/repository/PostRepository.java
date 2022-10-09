package com.example.springboot_basic.repository;

import com.example.springboot_basic.domain.member.Member;
import com.example.springboot_basic.domain.post.Post;
import com.example.springboot_basic.dto.post.PostSearch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    // 동적으로 쿼리를 넣을 수 없어서 상황마다 함수를 만들어줬음!!!
    // search.postTitle 이렇게 변수로 적어도 전부 작동하고, search.getPostTitle() 이렇게 getter 함수 써도 전부 작동함
    // 게시글 전체 조회 - 제목으로 검색
    @Query("select p from Post p where p.title Like %:#{#search.getPostTitle()}%")
    List<Post> findPostsByTitle(@Param("search") PostSearch postSearch);

    // 게시글 전체 조회 - 카테고리로 검색
    @Query("select p from Post p where p.category =:#{#search.getPostCategory()}")
    List<Post> findPostsByCategory(@Param("search") PostSearch postSearch);

    // 게시글 전체 조회 - 제목과 카테고리로 검색
    @Query("select p from Post p where p.title Like %:#{#search.getPostTitle()}% and p.category =:#{#search.getPostCategory()}")
    List<Post> findPosts(@Param("search") PostSearch postSearch);

    List<Post> findByMember(Member member);
}