package com.allchat.allchat.controller;

import com.allchat.allchat.dto.CMRespDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/auth")
@RequiredArgsConstructor
@RestController
public class AuthController {

    @PostMapping("/signup")
    public ResponseEntity<?> signup(){
        return new ResponseEntity<>(new CMRespDTO<>(1,"회원가입 성공",null), HttpStatus.OK);
    }


}
