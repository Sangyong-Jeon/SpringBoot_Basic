package com.example.springboot_basic.controller;

import com.example.springboot_basic.domain.member.Member;
import com.example.springboot_basic.dto.member.MemberInfoResponse;
import com.example.springboot_basic.dto.post.*;
import com.example.springboot_basic.security.PrincipalDetails;
import com.example.springboot_basic.service.PostService;
import com.example.springboot_basic.util.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Controller
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    public final ResponseUtil responseUtil;

    // 게시글 등록 페이지
    @GetMapping("/posts/new")
    public String createPost(Model model) {
        model.addAttribute("form", new PostForm());
        return "post/post-form";
    }

    // 게시글 등록
    @PostMapping("/posts/new")
    public String savePost(@ModelAttribute PostForm postForm,
                           RedirectAttributes redirectAttributes,
                           @AuthenticationPrincipal PrincipalDetails principalDetails) throws IOException {
        Member member = principalDetails.getMember();
        MemberInfoResponse memberInfo = new MemberInfoResponse(member);
        Long postId = postService.savePost(postForm, memberInfo);
        redirectAttributes.addAttribute("postId", postId);
        return "redirect:/posts";
    }

    // 게시글 리스트 페이지
    @GetMapping("/posts")
    public String listPost(@PageableDefault(size = 5) Pageable pageable,
                           @ModelAttribute("postSearch") PostSearch postSearch,
                           Model model) {
        PostsPageResponse postPages = postService.findPostsPaging(postSearch, pageable);
        model.addAttribute("postPages", postPages);
        return "post/post-list";
    }

    // 게시글 수정 페이지
    @GetMapping("/posts/{postId}/update")
    public String updatePostForm(@PathVariable("postId") Long postId, Model model) {
        PostEditResponse editPost = postService.findUpdatePost(postId);
        model.addAttribute("form", editPost);
        return "post/post-form-update";
    }

    // 게시글 상세조회 페이지
    @GetMapping("/posts/{postId}")
    public String postInfo(@PathVariable("postId") Long postId,
                           HttpServletRequest request,
                           HttpServletResponse response,
                           Model model) {
        PostInfoResponse postInfo = postService.findPost(postId, request, response);
        model.addAttribute("post", postInfo);
        return "post/post-info";
    }
}
