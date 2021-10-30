package com.allchat.allchat.controller;

import com.allchat.allchat.dto.CMRespDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/rooms")
@RestController
public class ChatRoomController {

    @PostMapping
    public ResponseEntity<?> createChatRoom(){
        return new ResponseEntity<>(new CMRespDTO<>(1, "채팅방 생성 성공", null), HttpStatus.CREATED);
    }
}
