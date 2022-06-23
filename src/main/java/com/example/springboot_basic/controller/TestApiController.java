//package com.example.springboot_basic.controller;
//
//import com.example.personalproject.dto.MemberDto;
//import com.example.personalproject.dto.response.Header;
//import com.example.personalproject.dto.response.ResponseData;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.multipart.MultipartFile;
//
//import javax.servlet.http.HttpServletRequest;
//import java.io.File;
//import java.io.IOException;
//import java.nio.charset.StandardCharsets;
//
//@RestController
//public class TestApiController {
//
//    @GetMapping("/test2")
//    public ResponseEntity getAllUsers() {
//        return new ResponseEntity(HttpStatus.OK);
//    }
//
//    // 클라이언트에 응답하는 ResponseEntity
//    @GetMapping("/response")
//    public ResponseEntity<ResponseData<MemberDto>> response() {
//        System.out.println("==================Body 만듬========+");
//        MemberDto memberDto = MemberDto.builder()
//                .id(1L)
//                .name("이름1")
//                .password("password")
//                .build();
//
//        Header responseHeader = Header.createHeader()
//                .status("200")
//                .code("OK")
//                .message("요청 성공")
//                .build();
//
//        ResponseData<MemberDto> responseData = ResponseData.<MemberDto>createResponseData()
//                .header(responseHeader)
//                .body(memberDto)
//                .build();
//
//        System.out.println(responseData);
//        System.out.println("==========================+");
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(new MediaType("application", "json", StandardCharsets.UTF_8));
//
//        return new ResponseEntity<ResponseData<MemberDto>>(responseData, headers, HttpStatus.OK);
//    }
//
//    // 파일 업로드
//    @PostMapping("/uploadFile")
//    public String addFile2(@RequestParam("File") MultipartFile uploadFile,
//                           HttpServletRequest request) {
//        String fileName = uploadFile.getOriginalFilename();
//        String saveUrl = request.getSession().getServletContext().getRealPath("/"); // 경로 : 프로젝트/src/main/webapp
//        String filePath = saveUrl + "image/" + fileName;
//        try {
//            uploadFile.transferTo(new File(filePath));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        System.out.println("fileName = " + fileName);
//        System.out.println("saveUrl = " + saveUrl);
//        System.out.println("filePath = " + filePath);
//        return filePath + " 파일 저장 완료";
//    }
//}
