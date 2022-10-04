package com.example.springboot_basic.exception;

public class CommentNotExistException extends RuntimeException {
    private final int errorCode;
    private static final long serialVersionUID = -8684309498440650358L;

    public CommentNotExistException(String message) {
        this(message, 404);
    }

    public CommentNotExistException(String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public int getErrCode() {
        return errorCode;
    }
}
