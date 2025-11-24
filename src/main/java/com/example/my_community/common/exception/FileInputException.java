package com.example.my_community.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// 잘못된 파일 저장 요청에 대해 400 코드 사용
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class FileInputException extends RuntimeException {
    public FileInputException() {
        super("잘못된 파일 요청");
    }

    public FileInputException(String message, Throwable cause) {
        super(message);
    }
}
