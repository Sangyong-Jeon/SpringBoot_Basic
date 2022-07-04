package com.example.springboot_basic.controller;

import com.example.springboot_basic.domain.member.Member;
import com.example.springboot_basic.dto.member.MemberForm;
import com.example.springboot_basic.dto.member.MemberInfoResponse;
import com.example.springboot_basic.security.PrincipalDetails;
import com.example.springboot_basic.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.File;
import java.io.IOException;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    // 회원가입
    @GetMapping("/members/new")
    public String createForm(Model model) {
        model.addAttribute("memberForm", new MemberForm());
        return "member/member-form";
    }

    @PostMapping(value = "members/new")
    public String create(@Valid MemberForm form, BindingResult result) {
        if (result.hasErrors()) {
            return "member/createMemberForm";
        }
        memberService.join(form);
        return "redirect:/";
    }

    // 회원 상세조회
    @GetMapping("/members")
    public String info(@AuthenticationPrincipal PrincipalDetails principalDetails,
                       Model model) {
        Member member = principalDetails.getMember();
        MemberInfoResponse memberInfoResponse = new MemberInfoResponse(member);
        model.addAttribute("member", memberInfoResponse);
        return "member/member-info";
    }
}
