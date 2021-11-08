package com.allchat.allchat.controller;

import com.allchat.allchat.dto.CMRespDTO;
import com.allchat.allchat.dto.user.UserDTO;
import com.allchat.allchat.dto.user.UserSignupDTO;
import com.allchat.allchat.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
public class AuthController {

    private final UserService userService;

    /**
     * 아이디 중복확인
     */
    @PostMapping("/auth/duplicate")
    public ResponseEntity<?> checkDuplicate(@Valid @RequestBody UserDTO userDTO, BindingResult bindingResult){

        Boolean result = userService.isDuplicateNickname(userDTO.getUsername());

        return new ResponseEntity<>(new CMRespDTO<>(1,"아이디 중복체크 성공", result), HttpStatus.OK);
    }


    /**
     * 회원가입
     */
    @PostMapping("/auth/signup")
    public ResponseEntity<?> signup(@Valid @RequestBody UserSignupDTO userSignupDTO, BindingResult bindingResult){

        userService.join(userSignupDTO);

        return new ResponseEntity<>(new CMRespDTO<>(1,"회원가입 성공",null), HttpStatus.OK);
    }


}
