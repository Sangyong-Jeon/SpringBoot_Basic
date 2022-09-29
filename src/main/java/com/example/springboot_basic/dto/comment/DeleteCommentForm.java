package com.example.springboot_basic.dto.comment;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class DeleteCommentForm {

    @NotNull(message = "댓글 번호가 들어오지 않았습니다.")
    private Long commentId;
    private String password;
}
