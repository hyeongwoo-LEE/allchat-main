package com.allchat.allchat.controller;

import com.allchat.allchat.config.auth.PrincipalDetails;
import com.allchat.allchat.dto.CMRespDTO;
import com.allchat.allchat.dto.chatRoom.ChatRoomDTO;
import com.allchat.allchat.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/chatrooms")
@RequiredArgsConstructor
@RestController
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    /**
     * 채팅방 목록
     */
    @GetMapping
    public ResponseEntity<?> chatRoomList(){
        return null;
    }

    /**
     * 채팅방 생성
     */
    @PostMapping
    public ResponseEntity<?> createChatRoom(@RequestBody ChatRoomDTO chatRoomDTO,
                                            @AuthenticationPrincipal PrincipalDetails principalDetails){

        chatRoomService.create(chatRoomDTO.getTitle(), principalDetails.getUser().getUserId());

        return new ResponseEntity<>(new CMRespDTO<>(1, "채팅방 생성 성공", null), HttpStatus.CREATED);
    }
}
