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

    @GetMapping("/members/new")
    public String createForm(Model model) {
        model.addAttribute("memberForm", new MemberForm());
        return "member/createMemberForm";
    }

    @PostMapping(value = "members/new")
    public String create(@Valid MemberForm form, BindingResult result) {
        if (result.hasErrors()) {
            return "member/createMemberForm";
        }
        memberService.join(form);
        return "redirect:/";
    }

    @GetMapping("/members")
    public String info(@AuthenticationPrincipal PrincipalDetails principalDetails,
                       Model model) {
        Member member = principalDetails.getMember();
        MemberInfoResponse memberInfoResponse = new MemberInfoResponse(member);
        model.addAttribute("member", memberInfoResponse);
        return "member/memberInfo";
    }

    @PostMapping("/test")
    public String addFile(@RequestParam String username,
                          @RequestParam MultipartFile file) throws IOException {
        System.out.println("username = " + username);

        if (!file.isEmpty()) {
            String fullPath = "/Users/yong/Documents/GitHub/personal/file/" + file.getOriginalFilename();
            System.out.println("파일 저장 fullPath = " + fullPath);
            file.transferTo(new File(fullPath));
        }
        return "test-form";
    }


}
