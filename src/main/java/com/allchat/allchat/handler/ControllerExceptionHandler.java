package com.allchat.allchat.handler;

import com.allchat.allchat.dto.CMRespDTO;
import com.allchat.allchat.handler.exception.CustomException;
import com.allchat.allchat.handler.exception.CustomValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;

@RestController
@ControllerAdvice
public class ControllerExceptionHandler {

    //유효성 검사
    @ExceptionHandler(CustomValidationException.class)
    public ResponseEntity<CMRespDTO<?>> validationApiException(CustomValidationException e){

        return new ResponseEntity<>(new CMRespDTO<>(-1,e.getMessage(),e.errorMap), HttpStatus.BAD_REQUEST);
    }

    //에러 처리
    @ExceptionHandler(CustomException.class)
    public ResponseEntity<CMRespDTO<?>> apiException(CustomException e){

        return new ResponseEntity<>(new CMRespDTO<>(-1,e.getMessage(),null), HttpStatus.BAD_REQUEST);
    }
}
