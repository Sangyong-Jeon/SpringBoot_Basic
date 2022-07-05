package com.example.springboot_basic.util;

import com.example.springboot_basic.dto.response.ResponseData;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class ResponseUtil {

    public ResponseEntity<ResponseData<?>> createResponseEntity(ResponseData<?> responseData, HttpHeaders headers) {
        String status = responseData.getHeader().getStatus();
        HttpStatus httpStatus;
        switch (status) {
            case "301":
                httpStatus = HttpStatus.MOVED_PERMANENTLY;
                break;
            case "400":
                httpStatus = HttpStatus.BAD_REQUEST;
                break;
            default:
                httpStatus = HttpStatus.OK;
                break;
        }
        return new ResponseEntity<>(responseData, headers, httpStatus);
    }
}
