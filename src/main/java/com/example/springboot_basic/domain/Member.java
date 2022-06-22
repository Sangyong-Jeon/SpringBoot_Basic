package com.example.springboot_basic.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@Table(name = "MEMBER") // 테이블명 변경
@SequenceGenerator( // 시퀀스 생성
        name = "MEM_SEQ_GENERATOR",
        sequenceName = "MEM_SEQ",
        initialValue = 1, allocationSize = 1)
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 기본생성자 PROTECTED 생성
public class Member extends BaseTimeEntity {

    @Id // 기본키 설정
    @Column(name = "MEM_ID") // 컬럼명 변경
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MEM_SEQ_GENERATOR") // 자동생성 시퀀스 적용
    private Long id;

    @Column(name = "ID", length = 20, unique = true, nullable = true) // 컬럼명 "ID", VARCHAR2(20), UNIQUE, NOT NULL
    private String loginId;

    @Column(name = "PW", length = 200, nullable = false)
    private String password;

    @Column(name = "NAME", length = 25, nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "ROLE", length = 20, nullable = false)
    private MemberRole role;
}
