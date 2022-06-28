package com.example.springboot_basic.security;

import com.example.springboot_basic.domain.member.Member;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

/*
시큐리티가 /login 주소 요청이 오면 낚아채서 로그인을 한다.
로그인 진행이 완료되면 시큐리티 session을 만든다. (Security ContextHolderfㅏ는 키값에 세션 정보 저장)
시큐리티 session에 들어갈 수 있는 오브젝트 타입은 Authentication 타입
Authentication 타입에는 User정보가 있어야하는데 이 User 타입은 UserDetails와 OAuth2User 타입만 올 수 있음.
UserDetails = 일반로그인, OAuth2User = 소셜로그인
Security Session => Authentication => UserDetails or OAuth2User
 */
@Getter
public class PrincipalDetails implements UserDetails {

    private Member member;

    public PrincipalDetails(Member member) {
        this.member = member;
    }

    // 해당 회원의 권한을 리턴하는 곳
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add((GrantedAuthority) () -> member.getRole().toString());
        return authorities;
    }

    @Override
    public String getPassword() {
        return member.getPassword();
    }

    @Override
    public String getUsername() {
        return member.getLoginId();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
