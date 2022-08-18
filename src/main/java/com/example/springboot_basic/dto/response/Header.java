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

    @Builder
    public Header(String status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }

    public static Header ok(String message) {
        return new Header("200", "OK", message);
    }

    public static Header movedPermanently(String message) {
        return new Header("301", "MOVED_PERMANENTLY", message);
    }

    public static Header badRequest(String message) {
        return new Header("400", "BAD_REQUEST", message);
    }

    public static Header notFound(String message) {
        return new Header("404", "NOT_FOUND", message);
    }
}
