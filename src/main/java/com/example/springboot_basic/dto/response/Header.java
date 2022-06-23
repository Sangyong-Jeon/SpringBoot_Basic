package com.example.springboot_basic.dto.response;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Header {
    private String status;
    private String code;
    private String message;

    @Builder(builderMethodName = "createHeader")
    Header(String status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
