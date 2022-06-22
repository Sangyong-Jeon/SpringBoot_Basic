package com.example.springboot_basic.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@SequenceGenerator(
        name = "COM_SEQ_GENERATOR",
        sequenceName = "COM_SEQ",
        initialValue = 1, allocationSize = 1)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseEntity {

    @Id
    @Column(name = "COM_ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "COM_SEQ_GENERATOR")
    private Long id;

    @Column(name = "NAME")
    private String name;

    @Column(name = "CONTENT")
    private String content;

    // @JoinColumn에는 referencedColumnName 속성이 있는데 이게 바로 대상 테이블의 PK를 지정하는 것이다.
    // 따라서 이 속성의 기본값으로는 현재 설정해놓은 대상 테이블의 PK를 지정한다.
    // 그러므로 "COM_PARENT_ID" 는 FK이고, 이 FK가 가리키는 곳이 Comment의 PK인 "COM_ID" 이다.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COM_PARENT_ID")
    private Comment parent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEM_ID")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "POST_ID")
    private Post post;
}
