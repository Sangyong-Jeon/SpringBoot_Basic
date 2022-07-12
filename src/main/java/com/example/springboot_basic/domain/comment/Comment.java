package com.example.springboot_basic.domain.comment;

import com.example.springboot_basic.domain.BaseEntity;
import com.example.springboot_basic.domain.member.Member;
import com.example.springboot_basic.domain.post.Post;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

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

    @Column(name = "CONTENT")
    private String content;

    // @JoinColumn에는 referencedColumnName 속성이 있는데 이게 바로 대상 테이블의 PK를 지정하는 것이다.
    // 따라서 이 속성의 기본값으로는 현재 설정해놓은 대상 테이블의 PK를 지정한다.
    // 그러므로 "COM_PARENT_ID" 는 FK이고, 이 FK가 가리키는 곳이 Comment의 PK인 "COM_ID" 이다.
    @ManyToOne(fetch = FetchType.LAZY) // 부모니까 N:1
    @JoinColumn(name = "COM_PARENT_ID")
    private Comment parent; // 계층구조

    @BatchSize(size = 100)
    @OneToMany(mappedBy = "parent") // 자식은 여러명 가질 수 있으니 1:N
    private List<Comment> child = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "MEM_ID")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "POST_ID")
    private Post post;

    @Builder
    public Comment(Long id, String content, Comment parent, Member member, Post post) {
        this.id = id;
        this.content = content;
        this.parent = parent;
        this.member = member;
        this.post = post;
    }
}
