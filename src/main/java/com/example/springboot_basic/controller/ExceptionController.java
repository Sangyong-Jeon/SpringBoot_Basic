package com.example.springboot_basic.controller;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@ControllerAdvice
public class ExceptionController {

    // 메서드가 잘못되었거나, 적합 또는 적절하지 않은 인자를 메서드에 전달 했을때 발생
    @ExceptionHandler(IllegalArgumentException.class)
    public String illegalArgumentExceptionResponse(IllegalArgumentException e, Model model) {
        model.addAttribute("status", "400");
        model.addAttribute("type", "BAD_REQUEST");
        model.addAttribute("timestamp", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")));
        model.addAttribute("message", e.getMessage());
        model.addAttribute("exception", e.getStackTrace());
        return "error/error";
    }
}
