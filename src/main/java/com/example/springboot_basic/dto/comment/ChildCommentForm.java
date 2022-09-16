package com.example.springboot_basic.dto.comment;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class ChildCommentForm {

    @NotBlank(message = "대댓글 내용을 올바르게 적어주세요.")
    private String content;

    @NotNull(message = "대댓글 번호가 들어오지 않았습니다.")
    private Long childId;

    private String password;

}

