package com.example.springboot_basic.dto.post;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class PostInfoResponse {

    private Long id;
    private String title;
    private String content;
    private int viewCount;
    private String createdName;
    private String createdDate;
    private String updatedDate;
    private List<FilesResponse> storeImageName = new ArrayList<>();
    private List<MultipartFile> imageFiles = new ArrayList<>();

    @Builder
    public PostInfoResponse(Long id, String title, String content, int viewCount, String createdName, String createdDate, String updatedDate, List<FilesResponse> storeImageName) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.viewCount = viewCount;
        this.createdName = createdName;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
        this.storeImageName = storeImageName;
    }
}
