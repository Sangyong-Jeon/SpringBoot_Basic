package com.example.springboot_basic.dto.comment;

import com.example.springboot_basic.domain.comment.Comment;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.format.DateTimeFormatter;

@Setter
@Getter
@NoArgsConstructor
public class CommentResponse {

    private Long id; // 댓글번호
    private String content; // 댓글내용
    private String createdBy; // 작성자
    private String createdDate; // 작성일시

    public CommentResponse(Comment comment) {
        this.id = comment.getId();
        this.content = comment.getContent();
        this.createdBy = comment.getCreatedBy();
        this.createdDate = comment.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
