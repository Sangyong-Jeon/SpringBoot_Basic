package com.example.springboot_basic.exception;

/*
사용자 정의 예외
 */
public class PostNotExsistException extends RuntimeException {

    private final int ERR_CODE;
    private static final long serialVersionUID = 1L;

    public PostNotExsistException(String message) { // 문자열을 매개변수로 받는 생성자
        this(message, 404);
    }

    public PostNotExsistException(String message, int errCode) { // 문자열을 매개변수로 받는 생성자
        super(message);
        ERR_CODE = errCode;
    }

    public int getErrCode() {
        return ERR_CODE;
    }
}
