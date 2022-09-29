package com.example.springboot_basic.security;

/*
SecurityConfig에서 formLogin().loginProcessingUrl("/login")을 설정했으면
/login 요청이 올 때 자동으로 UserDetailsService 타입으로 IoC되어 있는 loadUserByUsername  함수 실행됨.
 */

import com.example.springboot_basic.domain.member.Member;
import com.example.springboot_basic.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PrincipalDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    // loadUserByUsername()이 종료되면 @AuthenticationPrincipal 어노테이션 만들어짐
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return memberRepository.findByLoginId(username)
                .map(PrincipalDetails::new)
                .orElse(null);
    }
}
