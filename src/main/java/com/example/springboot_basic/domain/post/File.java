package com.example.springboot_basic.domain.post;

import com.example.springboot_basic.domain.BaseTimeEntity;
import com.example.springboot_basic.dto.UploadFile;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Getter
@SequenceGenerator(
        name = "FILE_SEQ_GENERATOR",
        sequenceName = "FILE_SEQ",
        initialValue = 1, allocationSize = 1)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class File extends BaseTimeEntity {

    @Id
    @Column(name = "FILE_ID")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FILE_SEQ_GENERATOR")
    private Long id;

    @Column(name = "UPLOAD_FILE_NAME")
    private String uploadFileName;

    @Column(name = "STORE_FILE_NAME")
    private String storeFileName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "POST_ID")
    private Post post;

    @Builder
    public File(String uploadFileName, String storeFileName, Post post) {
        this.uploadFileName = uploadFileName;
        this.storeFileName = storeFileName;
        this.post = post;
    }

    public File(UploadFile storeImageFile, Post post) {
        this.uploadFileName = storeImageFile.getUploadFileName();
        this.storeFileName = storeImageFile.getStoreFileName();
        this.post = post;
    }
}
