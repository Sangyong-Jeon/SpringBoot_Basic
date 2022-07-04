package com.example.springboot_basic.dto.response;

import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ResponseData<T> {
    private Header header;
    private T body;

    @Builder
    public ResponseData(Header header, T body) {
        this.header = header;
        this.body = body;
    }
}
