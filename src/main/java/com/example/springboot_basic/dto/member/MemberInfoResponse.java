package com.example.springboot_basic.dto.member;

import com.example.springboot_basic.domain.comment.Comment;
import com.example.springboot_basic.domain.member.Member;
import com.example.springboot_basic.domain.member.MemberRole;
import com.example.springboot_basic.domain.post.Post;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MemberInfoResponse {
    private Long id;
    private String loginId;
    private String name;
    private MemberRole role;
    private int postCount;
    private int commentCount;
    private List<PostDto> postDtos = new ArrayList<>();
    private List<CommentDto> commentDtos = new ArrayList<>();

    public MemberInfoResponse(Member member) {
        this.id = member.getId();
        this.loginId = member.getLoginId();
        this.name = member.getName();
        this.role = member.getRole();
    }

    @Getter
    @NoArgsConstructor
    public static class PostDto {
        private Long id;
        private String title;
        private LocalDateTime createdDate;

        public PostDto(Post post) {
            id = post.getId();
            title = post.getTitle();
            createdDate = post.getCreatedDate();
        }
    }

    @Getter
    @NoArgsConstructor
    public static class CommentDto {
        private Long id;
        private String content;
        private LocalDateTime createdDate;
        private String postTitle;

        public CommentDto(Comment comment) {
            this.id = comment.getId();
            this.content = comment.getContent();
            this.createdDate = comment.getCreatedDate();
            this.postTitle = comment.getPost().getTitle();
        }
    }
}
