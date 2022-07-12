package com.example.springboot_basic.dto.comment;

import com.example.springboot_basic.domain.comment.Comment;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Setter
@Getter
@NoArgsConstructor
public class CommentResponse {

    private Long id; // 댓글번호
    private String content; // 댓글내용
    private String createdBy; // 작성자
    private String createdDate; // 작성일시
    private List<CommentResponse> childComments = new ArrayList<>();

    public CommentResponse(Comment comment) {
        this.id = comment.getId();
        this.content = comment.getContent();
        this.createdBy = comment.getCreatedBy();
        this.createdDate = comment.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
        /* 문제점
        댓글을 조회할 때 대댓글이 있는지 확인하려고 child 사이즈를 확인해야하는데 이 때 select 쿼리가 날라간다.
        그 후 사이즈가 0 이상이면 대댓글을 꺼내서 dto로 변환하는데 똑같은 CommentResponse를 사용하기 때문에 대댓글도 child가 있는지 확인하게 된다.
        따라서 대댓글도 대대댓글이 있는지 확인하려고 select 쿼리를 날리게된다.

        해결방법
        1. 대댓글 dto를 새로 만든다.
        2. 댓글 엔티티에서 대댓글 fk들을 따로 in절 조회하여 전부 조회한 다음 그걸 따로 대댓글 dto로 변환하여 부모의 pk에 맞게 댓글 dto를 넣어준다.
        */
        if (comment.getChild().size() > 0)
            this.childComments = comment.getChild().stream().map(CommentResponse::new).collect(Collectors.toList());
    }
}
