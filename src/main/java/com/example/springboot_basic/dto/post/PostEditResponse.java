package com.example.springboot_basic.dto.post;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
public class PostEditResponse {
    private Long id;
    private String title;
    private String content;
    private List<FilesResponse> storeImageNames = new ArrayList<>();

    public PostEditResponse(Long id, String title, String content, List<FilesResponse> storeImageNames) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.storeImageNames = storeImageNames;
    }
}
