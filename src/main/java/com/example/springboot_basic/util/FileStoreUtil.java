package com.example.springboot_basic.util;


import com.example.springboot_basic.dto.UploadFile;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

// 멀티파트 파일을 서버에 저장하는 역할을 담당한다.
@Component
public class FileStoreUtil {

    @Value("${file.dir}")
    private String fileDir;

    public String getFullPath(String filename) {
        return fileDir + filename;
    }

    public List<UploadFile> storeFiles(List<MultipartFile> multipartFiles) throws IOException {
        List<UploadFile> storeFileResult = new ArrayList<>();
        for (MultipartFile multipartFile : multipartFiles) {
            if (!multipartFile.isEmpty()) {
                storeFileResult.add(storeFile(multipartFile));
            }
        }
        return storeFileResult;
    }

    public UploadFile storeFile(MultipartFile multipartFile) throws IOException {
        if (multipartFile.isEmpty()) return null;

        String uploadFileName = multipartFile.getOriginalFilename();
        //서버에 저장하는 파일명
        String storeFileName = createStoreFileName(uploadFileName);
        multipartFile.transferTo(new File(getFullPath(storeFileName)));
        return new UploadFile(uploadFileName, storeFileName);
    }

    // 서버 내부에서 관리하는 파일명은 유일한 이름을 생성하는 UUID를 사용해서 충돌나지 않도록 한다.
    private String createStoreFileName(String originalFilename) {
        String uuid = UUID.randomUUID().toString();
        String ext = extractExt(originalFilename);
        return uuid + "." + ext;
    }

    // 확장자를 별도로 추출해서 서버 내부에서 관리하는 파일명에도 붙여준다.
    // 예를 들어 고객이 a.png라는 이름으로 업로드하면 510290a-asf9d230-asf9.png와 같이 저장한다.
    private String extractExt(String originalFilename) {
        int pos = originalFilename.lastIndexOf(".");
        return originalFilename.substring(pos + 1);
    }
}
