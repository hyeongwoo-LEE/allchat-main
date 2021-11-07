package com.allchat.allchat.service;

import com.allchat.allchat.domain.chatRoom.ChatRoom;
import com.allchat.allchat.domain.chatRoom.ChatRoomRepository;
import com.allchat.allchat.domain.chatRoomJoin.ChatRoomJoin;
import com.allchat.allchat.domain.chatRoomJoin.ChatRoomJoinRepository;
import com.allchat.allchat.domain.chatRoomJoin.RoleType;
import com.allchat.allchat.domain.user.User;
import com.allchat.allchat.dto.chatRoom.ChatRoomDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomJoinRepository chatRoomJoinRepository;

    /**
     * 채팅방 생성
     */
    @Transactional
    public ChatRoom create(ChatRoomDTO chatRoomDTO, Long principalId){

        ChatRoom chatRoom = ChatRoom.builder()
                .user(User.builder().userId(principalId).build())
                .title(chatRoomDTO.getTitle())
                .build();

        chatRoomRepository.save(chatRoom);

        ChatRoomJoin chatRoomJoin = ChatRoomJoin.builder()
                .user(User.builder().userId(principalId).build())
                .chatRoom(chatRoom)
                .role(RoleType.MASTER)
                .build();

        chatRoomJoinRepository.save(chatRoomJoin);

        return chatRoom;
    }


}
