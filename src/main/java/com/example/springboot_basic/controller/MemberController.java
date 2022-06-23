package com.example.springboot_basic.controller;

import com.example.springboot_basic.dto.member.MemberForm;
import com.example.springboot_basic.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

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

    @GetMapping("/members/info")
    public String info() {
        return "member/memberInfo";
    }
}
