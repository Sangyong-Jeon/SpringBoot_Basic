package com.example.springboot_basic.dto.post;

import com.example.springboot_basic.domain.post.PostCategory;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
public class PostSearch {

    private String postTitle; // 게시글 제목
    private PostCategory postCategory; // 게시글 카테고리[NOTICE, BOARD]

    public boolean isEmpty() {
        return postTitle == null && postCategory == null;
    }
}
