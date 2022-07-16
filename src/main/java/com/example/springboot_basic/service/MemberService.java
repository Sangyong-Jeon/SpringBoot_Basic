package com.example.springboot_basic.service;

import com.example.springboot_basic.domain.member.Member;
import com.example.springboot_basic.dto.member.MemberForm;
import com.example.springboot_basic.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    // 회원가입
    public void join(MemberForm memberForm) {
        validateDuplicateMember(memberForm);
        Member member = Member.builder()
                .loginId(memberForm.getLoginId())
                .password(bCryptPasswordEncoder.encode(memberForm.getPassword()))
                .name(memberForm.getName())
                .role(memberForm.getRole())
                .build();
        memberRepository.save(member);
    }

    private void validateDuplicateMember(MemberForm memberForm) {
        Optional<Member> findMember = memberRepository.findByLoginId(memberForm.getLoginId());
        if (findMember.isPresent()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }
}
