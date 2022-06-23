package com.example.springboot_basic.config;

import com.example.springboot_basic.security.PrincipalDetails;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

@Configuration
public class AuditorAwareConfig implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication.getPrincipal().equals("anonymousUser")) {
            return Optional.of("anonymousUser");
        }
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();
        return Optional.of(principalDetails.getMember().getName());
    }
}
