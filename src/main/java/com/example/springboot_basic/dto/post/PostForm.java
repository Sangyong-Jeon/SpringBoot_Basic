package com.example.springboot_basic.dto.post;

import com.example.springboot_basic.domain.post.PostCategory;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class PostForm {

    private String title;
    private String content;
    private PostCategory category;
    private MultipartFile attachFile;
    private List<MultipartFile> imageFiles = new ArrayList<>();
}
