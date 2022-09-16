package com.example.springboot_basic.api;

import com.example.springboot_basic.dto.comment.ChildCommentForm;
import com.example.springboot_basic.dto.comment.CommentForm;
import com.example.springboot_basic.dto.response.ResponseData;
import com.example.springboot_basic.service.CommentService;
import com.example.springboot_basic.util.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class CommentApiController {

    private final ResponseUtil responseUtil;
    private final CommentService commentService;

    // 댓글 및 대댓글 등록
    @PostMapping("/comments")
    public ResponseEntity<ResponseData<?>> addComment(@Valid @RequestBody CommentForm commentForm) {
        ResponseData<String> responseData = commentService.addComment(commentForm);
        return responseUtil.createResponseEntity(responseData, new HttpHeaders());
    }

    // 댓글 수정
    @PatchMapping("/comments")
    public ResponseEntity<ResponseData<?>> updateComment(@Valid @RequestBody CommentForm commentForm) {
        ResponseData<String> responseData = commentService.updateComment(commentForm);
        return responseUtil.createResponseEntity(responseData, new HttpHeaders());
    }

    // 대댓글 수정
    @PatchMapping("/childs")
    public ResponseEntity<ResponseData<?>> updateChildComment(@Valid @RequestBody ChildCommentForm childForm) {
        ResponseData<String> responseData = commentService.updateChildComment(childForm);
        return responseUtil.createResponseEntity(responseData, new HttpHeaders());
    }
}
