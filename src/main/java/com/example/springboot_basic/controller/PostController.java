package com.example.springboot_basic.controller;

import com.example.springboot_basic.domain.member.Member;
import com.example.springboot_basic.dto.member.MemberInfoResponse;
import com.example.springboot_basic.dto.post.PostForm;
import com.example.springboot_basic.dto.post.PostInfoResponse;
import com.example.springboot_basic.dto.post.PostsResponse;
import com.example.springboot_basic.file.FileStore;
import com.example.springboot_basic.security.PrincipalDetails;
import com.example.springboot_basic.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;


    @GetMapping("/posts/new")
    public String createPost(Model model) {
        model.addAttribute("form", new PostForm());
        return "post/post-form";
    }

    @PostMapping("/posts/new")
    public String savePost(@ModelAttribute PostForm postForm,
                           RedirectAttributes redirectAttributes,
                           @AuthenticationPrincipal PrincipalDetails principalDetails) throws IOException {
        System.out.println("postForm = " + postForm);
        Member member = principalDetails.getMember();
        MemberInfoResponse memberInfo = new MemberInfoResponse(member);

        Long postId = postService.savePost(postForm, memberInfo);

        redirectAttributes.addAttribute("postId", postId);
        return "redirect:/posts/{postId}";
    }

    // 게시글 목록
    @GetMapping("/posts")
    public String listPost(Model model) {
        List<PostsResponse> posts = postService.findPosts();
        model.addAttribute("posts", posts);
        return "post/postList";
    }

    // 게시글 수정
    @GetMapping("/posts/{postId}/edit")
    public String updatePostForm(@PathVariable("postId") Long postId, Model model) {
        PostInfoResponse postInfo = postService.findPost(postId);
        model.addAttribute("form", postInfo);
        return "post/updatePostForm";
    }

    @PostMapping("/posts/{postId}/edit")
    public String updatePost(@ModelAttribute("form") PostInfoResponse form, RedirectAttributes redirectAttributes) throws IOException {
        Long postId = postService.updatePost(form);
        redirectAttributes.addAttribute("postId", postId);
        return "redirect:/posts/{postId}";
    }
}
