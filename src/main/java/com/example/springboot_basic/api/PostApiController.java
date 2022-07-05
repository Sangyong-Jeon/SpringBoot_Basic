package com.example.springboot_basic.api;

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
    public ResponseEntity<ResponseData<?>> updatePost(@ModelAttribute PostInfoResponse form,
                                                      @PathVariable("postId") Long postId) throws IOException, URISyntaxException {
        System.out.println("form = " + form);
        ResponseData<String> responseData = postService.updatePost(form);
        HttpHeaders headers = new HttpHeaders();// 헤더를 설정하기 위해 생성
        headers.setLocation(new URI("http://localhost:8081/posts/" + postId));// 헤더에 location 추가
        return responseUtil.createResponseEntity(responseData, headers);
    }

    // 게시글 삭제
    @DeleteMapping("/posts/{postId}")
    public ResponseEntity<ResponseData<?>> deletePost(@PathVariable("postId") Long postId) {
        ResponseData<String> responseData = postService.deletePost(postId);
        return responseUtil.createResponseEntity(responseData, new HttpHeaders());
    }
}
