package com.example.springboot_basic.dto.response;

import lombok.*;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ResponseData<T> {
    private Header header;
    private T body;

    @Builder(builderMethodName = "createResponseData")
    public ResponseData(Header header, T body) {
        this.header = header;
        this.body = body;
    }
}
