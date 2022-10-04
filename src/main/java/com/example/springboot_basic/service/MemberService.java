package com.example.springboot_basic.service;

import com.example.springboot_basic.domain.member.Member;
import com.example.springboot_basic.dto.member.MemberForm;
import com.example.springboot_basic.dto.response.Header;
import com.example.springboot_basic.dto.response.ResponseData;
import com.example.springboot_basic.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    // 회원가입
    public void join(MemberForm memberForm) {
        Member member = Member.builder()
                .loginId(memberForm.getLoginId())
                .password(bCryptPasswordEncoder.encode(memberForm.getPassword()))
                .name(memberForm.getName())
                .role(memberForm.getRole())
                .build();
        memberRepository.save(member);
    }

    public ResponseData<String> loginIdDuplicate(String loginId) {
        if (memberRepository.findByLoginId(loginId).isPresent()) {
            return new ResponseData<>(Header.badRequest("존재하는 아이디입니다."), "");
        }
        return new ResponseData<>(Header.ok("사용가능한 아이디입니다."), "");
    }
}
