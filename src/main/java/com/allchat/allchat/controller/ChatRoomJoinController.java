package com.allchat.allchat.controller;


import com.allchat.allchat.config.auth.PrincipalDetails;
import com.allchat.allchat.dto.CMRespDTO;
import com.allchat.allchat.dto.chatRoomJoin.ChatRoomJoinDTO;
import com.allchat.allchat.service.ChatRoomJoinService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
public class ChatRoomJoinController {

    private final ChatRoomJoinService chatRoomJoinService;

    /**
     * 채팅방 참여목록
     */
    @GetMapping("/chatrooms/{chatRoomId}/joins")
    public ResponseEntity<?> chatRoomJoinList(){
        return null;
    }

    /**
     * 채팅방 참여
     */
    @PostMapping("/joins")
    public ResponseEntity<?> join(@RequestBody ChatRoomJoinDTO chatRoomJoinDTO,
                                  @AuthenticationPrincipal PrincipalDetails principalDetails){

        if(!principalDetails.getUser().getUserId().equals(chatRoomJoinDTO.getUserId())){
            throw new IllegalStateException("참여 권한이 없습니다.");
        }

        chatRoomJoinService.join(chatRoomJoinDTO.getChatRoomId(), chatRoomJoinDTO.getUserId());

        return new ResponseEntity<>(new CMRespDTO<>(1, "채팅방 참여 성공", null), HttpStatus.CREATED);
    }

    /**
     * 채팅방 나가기
     */
    @DeleteMapping("/joins/{chatRoomJoinId}")
    public ResponseEntity<?> getOut(@RequestBody ChatRoomJoinDTO chatRoomJoinDTO,
                                    @AuthenticationPrincipal PrincipalDetails principalDetails){

        if(!principalDetails.getUser().getUserId().equals(chatRoomJoinDTO.getUserId())){
            throw new IllegalStateException("채팅방 나가기 권한이 없습니다.");
        }

        chatRoomJoinService.remove(chatRoomJoinDTO.getChatRoomId(), chatRoomJoinDTO.getUserId());

        return new ResponseEntity<>(new CMRespDTO<>(1, "채팅방 나가기 성공", null), HttpStatus.OK);
    }

}
