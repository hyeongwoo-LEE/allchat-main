package com.allchat.allchat.controller;

import com.allchat.allchat.config.auth.PrincipalDetails;
import com.allchat.allchat.dto.CMRespDTO;
import com.allchat.allchat.dto.chatRoom.ChatRoomDTO;
import com.allchat.allchat.dto.chatRoom.ChatRoomResDTO;
import com.allchat.allchat.service.ChatRoomService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/chatrooms")
@RequiredArgsConstructor
@RestController
public class ChatRoomController {

    private final ChatRoomService chatRoomService;

    /**
     * 전체 채팅방 목록
     */
    @GetMapping
    public ResponseEntity<?> allChatRoomList(@AuthenticationPrincipal PrincipalDetails principalDetails){

        List<ChatRoomResDTO> allChatRoomList = chatRoomService.getAllChatRoomList(principalDetails.getUser().getUserId());

        return new ResponseEntity<>(new CMRespDTO<>(1, "전체 채팅방 리스트 불러오기 성공", allChatRoomList), HttpStatus.OK);
    }

    /**
     * 참여중인 채팅방 목록
     */
    @GetMapping("/participating")
    public ResponseEntity<?> joinChatRoomList(@AuthenticationPrincipal PrincipalDetails principalDetails){

        List<ChatRoomResDTO> joinChatRoomList = chatRoomService.getJoinChatRoomList(principalDetails.getUser().getUserId());

        return new ResponseEntity<>(new CMRespDTO<>(1, "참여중인 채팅방 리스트 불러오기 성공", joinChatRoomList), HttpStatus.OK);
    }


    /**
     * 채팅방 생성
     */
    @PostMapping
    public ResponseEntity<?> createChatRoom(@RequestBody ChatRoomDTO chatRoomDTO,
                                            @AuthenticationPrincipal PrincipalDetails principalDetails){

        if (!principalDetails.getUser().getUserId().equals(chatRoomDTO.getMasterId())){
            throw new IllegalStateException("생성 권한이 없습니다.");
        }

        chatRoomService.create(chatRoomDTO, principalDetails.getUser().getUserId());

        return new ResponseEntity<>(new CMRespDTO<>(1, "채팅방 생성 성공", null), HttpStatus.CREATED);
    }

    /**
     * 채팅방 삭제
     */
    @DeleteMapping("/{chatRoomId}")
    public ResponseEntity<?> deleteChatRoom(@PathVariable Long chatRoomId){

        chatRoomService.remove(chatRoomId);

        return new ResponseEntity<>(new CMRespDTO<>(1, "채팅방 삭제 성공", null), HttpStatus.OK);
    }
}
