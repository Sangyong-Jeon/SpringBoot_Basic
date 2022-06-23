package com.example.springboot_basic.dto.member;

import com.example.springboot_basic.domain.member.Member;
import com.example.springboot_basic.domain.member.MemberRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberInfoResponse {
    private Long id;
    private String loginId;
    private String name;
    private MemberRole role;

    public MemberInfoResponse(Member member) {
        this.id = member.getId();
        this.loginId = member.getLoginId();
        this.name = member.getName();
        this.role = member.getRole();
    }
}
