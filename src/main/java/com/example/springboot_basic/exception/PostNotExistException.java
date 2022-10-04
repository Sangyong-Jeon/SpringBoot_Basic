package com.example.springboot_basic.exception;

/*
사용자 정의 예외
 */
public class PostNotExistException extends RuntimeException {

    private final int errorCode;
    private static final long serialVersionUID = -8454620355642518343L;

    public PostNotExistException(String message) { // 문자열을 매개변수로 받는 생성자
        this(message, 404);
    }

    public PostNotExistException(String message, int errorCode) { // 문자열을 매개변수로 받는 생성자
        super(message);
        this.errorCode = errorCode;
    }

    public int getErrCode() {
        return errorCode;
    }
}
