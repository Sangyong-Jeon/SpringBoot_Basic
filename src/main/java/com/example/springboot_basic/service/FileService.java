package com.example.springboot_basic.service;

import com.example.springboot_basic.domain.post.File;
import com.example.springboot_basic.file.FileStore;
import com.example.springboot_basic.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FileService {

    private final FileRepository fileRepository;
    private final FileStore fileStore;


    public ResponseEntity<UrlResource> downloadImage(Long fileId) throws MalformedURLException {
        Optional<File> findFile = fileRepository.findById(fileId);
        File file = findFile.orElse(null);
        if (file == null) return null;
        String storeFileName = file.getStoreFileName();
        String uploadFileName = file.getUploadFileName();
        UrlResource resource = new UrlResource("file:" + fileStore.getFullPath(storeFileName));
        String contentDisposition = "attachment; filename=\"" + uploadFileName + "\"";
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition)
                .body(resource);
    }

    public ResponseEntity deleteImage(Long fileId) {
        Optional<File> findFile = fileRepository.findById(fileId);
        File file = findFile.orElse(null);
        if (file == null) return new ResponseEntity(HttpStatus.BAD_REQUEST);
        String fullPath = fileStore.getFullPath(file.getStoreFileName());
        java.io.File imageFile = new java.io.File(fullPath);
        if (imageFile.exists()) imageFile.delete();
        fileRepository.delete(file);
        return new ResponseEntity(HttpStatus.OK);
    }
}
