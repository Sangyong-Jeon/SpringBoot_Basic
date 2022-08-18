package com.example.springboot_basic.api;

import com.example.springboot_basic.dto.post.PostForm;
import com.example.springboot_basic.dto.post.PostInfoResponse;
import com.example.springboot_basic.dto.response.ResponseData;
import com.example.springboot_basic.service.PostService;
import com.example.springboot_basic.util.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PostApiController {

    private final PostService postService;
    public final ResponseUtil responseUtil;

    @GetMapping("/posts/{postId}")
    public ResponseEntity<ResponseData<?>> validatePost(@PathVariable Long postId) {
        ResponseData<String> responseData = postService.validatePost(postId);
        return responseUtil.createResponseEntity(responseData, new HttpHeaders());
    }

    @PatchMapping("/posts/{postId}")
    public ResponseEntity<ResponseData<?>> updatePost(@ModelAttribute PostForm form,
                                                      @PathVariable("postId") Long postId) throws IOException, URISyntaxException {
        ResponseData<String> responseData = postService.updatePost(form, postId);
        return responseUtil.createResponseEntity(responseData, new HttpHeaders());
    }

    // 게시글 삭제
    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<ResponseData<?>> deletePost(@PathVariable("postId") Long postId) {
        ResponseData<String> responseData = postService.deletePost(postId);
        return responseUtil.createResponseEntity(responseData, new HttpHeaders());
    }
}
