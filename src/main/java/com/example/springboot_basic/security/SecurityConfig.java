package com.example.springboot_basic.security;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity // Spring Security 설정 활성화
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http.authorizeRequests()
                .mvcMatchers("/", "/members/new", "/favicon.ico", "/error", "/css/**", "/js/**", "/image/**").permitAll()
                .mvcMatchers(HttpMethod.PATCH, "/api/comments", "/api/childs").permitAll()
                .mvcMatchers(HttpMethod.POST, "/api/comments").permitAll()
                .mvcMatchers(HttpMethod.GET, "/posts", "/posts/*", "/api/**").permitAll()
                .mvcMatchers(HttpMethod.POST, "/posts").hasAnyRole("ADMIN", "MANAGER", "USER") // hasAnyRole과 hasRole 함수는 자동으로 "ROLE_"이 붙음.
//                .mvcMatchers("/members").hasAnyRole("ADMIN", "USER")
                .anyRequest().authenticated() // 위 요청 외에는 모두 로그인 해야함
                .and()
                .formLogin()
                .and()
                .logout().logoutSuccessUrl("/")
                .and()
                .csrf().disable() // csrf 비활성화
                .build();
    }

//    // 인증하지 않을 주소 추가, static 폴더에서 설정
//    @Bean
//    public WebSecurityCustomizer webSecurityCustomizer() {
//        return (web) -> web.ignoring()
//                .mvcMatchers("/css/**", "/js/**", "/image/**")
//                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
//    }
}