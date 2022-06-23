package com.example.springboot_basic.dto.post;

import com.example.springboot_basic.domain.post.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.format.DateTimeFormatter;

@Getter
@Setter
@NoArgsConstructor
public class PostsResponse {
    private Long id;
    private String title;
    private int viewCount;
    private String createdName;
    private String createdDate;

    public PostsResponse(Long id, String title, int viewCount, String createdName, String createdDate) {
        this.id = id;
        this.title = title;
        this.viewCount = viewCount;
        this.createdName = createdName;
        this.createdDate = createdDate;
    }

    public PostsResponse(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.viewCount = post.getViewCount();
        this.createdName = post.getCreatedBy();
        this.createdDate = post.getCreatedDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
