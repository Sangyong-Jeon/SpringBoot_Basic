package com.example.springboot_basic.controller;

import com.example.springboot_basic.dto.response.Header;
import com.example.springboot_basic.dto.response.ResponseData;
import com.example.springboot_basic.exception.CommentNotExistException;
import com.example.springboot_basic.exception.PostNotExistException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@ControllerAdvice
public class ExceptionController {

    // 메서드가 잘못되었거나, 적합 또는 적절하지 않은 인자를 메서드에 전달 했을때 발생
    @ExceptionHandler(IllegalArgumentException.class)
    public String illegalArgumentExceptionHandler(IllegalArgumentException e, Model model) {
        model.addAttribute("status", "400");
        model.addAttribute("type", "BAD_REQUEST");
        model.addAttribute("timestamp", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")));
        model.addAttribute("message", e.getMessage());
        model.addAttribute("exception", e.getStackTrace());
        return "error/error";
    }

    @ExceptionHandler(PostNotExistException.class)
    public ResponseEntity<ResponseData<String>> postNotValidExceptionHandler(PostNotExistException e) {
        Header header = Header.notFound(e.getMessage());
        ResponseData<String> responseData = new ResponseData<>(header, "");
        return new ResponseEntity<>(responseData, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(CommentNotExistException.class)
    public ResponseEntity<ResponseData<String>> commentNotValidExceptionHandler(CommentNotExistException e) {
        Header header = Header.notFound(e.getMessage());
        ResponseData<String> responseData = new ResponseData<>(header, "");
        return new ResponseEntity<>(responseData, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ResponseData<String>> controllerValidException(MethodArgumentNotValidException e) {
        FieldError fieldError = e.getBindingResult().getFieldError();
        log.warn("MethodArgumentNotValidException 발생 : [{}] {} : 입력된 값 [{}]", fieldError.getField(), fieldError.getDefaultMessage(), fieldError.getRejectedValue());
        Header header = Header.badRequest(e.getBindingResult().getFieldError().getDefaultMessage());
        ResponseData<String> responseData = new ResponseData<>(header, "");
        return new ResponseEntity<>(responseData, HttpStatus.BAD_REQUEST);
    }
}
