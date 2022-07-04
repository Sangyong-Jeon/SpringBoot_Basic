package com.example.springboot_basic.controller;

import com.example.springboot_basic.file.FileStore;
import com.example.springboot_basic.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.net.MalformedURLException;

@Controller
@RequiredArgsConstructor
public class UtilController {

    private final FileStore fileStore;
    private final FileService fileService;

    // <img> 태그로 이미지를 조회할 때 사용한다.
    // UrlResource로 이미지 파일을 읽어서 @ResponseBody로 이미지 바이너리를 반환한다.
    @ResponseBody
    @GetMapping("/images/{filename}")
    public Resource showImage(@PathVariable("filename") String filename) throws MalformedURLException {
        return new UrlResource("file:" + fileStore.getFullPath(filename));
    }

    /*
    파일을 다운로드할 때 실행된다. 이미지 id로 요청한다.
    파일 다운로드시에는 고객이 업로드한 파일 이름으로 다운로드 하는게 좋다.
    이 때는 content-Disposition 헤더에 attachment; filename="업로드 파일명" 값을 주면 된다.
     */
    @GetMapping("/images/{fileId}/attach")
    public ResponseEntity<UrlResource> downloadImage(@PathVariable("fileId") Long fileId) throws MalformedURLException {
        return fileService.downloadImage(fileId);
    }

    // 파일 삭제
    @DeleteMapping("/images/{fileId}")
    public ResponseEntity<Object> deleteImage(@PathVariable("fileId") Long fileId) {
        return fileService.deleteImage(fileId);
    }
}
