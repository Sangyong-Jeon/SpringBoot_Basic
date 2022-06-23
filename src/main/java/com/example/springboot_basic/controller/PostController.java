package com.example.springboot_basic.controller;

import com.example.springboot_basic.domain.member.Member;
import com.example.springboot_basic.dto.member.MemberInfoResponse;
import com.example.springboot_basic.dto.post.PostForm;
import com.example.springboot_basic.security.PrincipalDetails;
import com.example.springboot_basic.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

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
}
