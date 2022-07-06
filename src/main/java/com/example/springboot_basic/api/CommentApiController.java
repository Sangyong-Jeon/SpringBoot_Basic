package com.example.springboot_basic.api;

import com.example.springboot_basic.dto.comment.CommentForm;
import com.example.springboot_basic.dto.response.ResponseData;
import com.example.springboot_basic.service.CommentService;
import com.example.springboot_basic.util.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
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

    // 댓글 등록
    @PostMapping("/comment")
    public ResponseEntity<ResponseData<?>> addComment(@Valid @RequestBody CommentForm commentForm) {
        System.out.println("commentForm = " + commentForm);
        ResponseData<String> responseData = commentService.addComment(commentForm);
        return responseUtil.createResponseEntity(responseData, new HttpHeaders());
    }
}
