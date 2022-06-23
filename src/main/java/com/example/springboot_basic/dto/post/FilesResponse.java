package com.example.springboot_basic.dto.post;

import com.example.springboot_basic.domain.post.File;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FilesResponse {
    private Long id;
    private String uploadFileName;
    private String storeFileName;

    public FilesResponse(File file) {
        this.id = file.getId();
        this.uploadFileName = file.getUploadFileName();
        this.storeFileName = file.getStoreFileName();
    }
}
