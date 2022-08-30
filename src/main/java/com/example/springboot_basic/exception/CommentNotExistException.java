package com.example.springboot_basic.exception;

public class CommentNotExistException extends RuntimeException {
    private final int ERR_CODE;
    private static final long serialVersionUID = -8684309498440650358L;

    public CommentNotExistException(String message) {
        this(message, 404);
    }

    public CommentNotExistException(String message, int errCode) {
        super(message);
        ERR_CODE = errCode;
    }

    public int getErrCode() {
        return ERR_CODE;
    }
}
