package com.allchat.allchat.service;

import com.allchat.allchat.domain.chatRoom.ChatRoom;
import com.allchat.allchat.domain.chatRoom.ChatRoomRepository;
import com.allchat.allchat.domain.chatRoomJoin.ChatRoomJoin;
import com.allchat.allchat.domain.chatRoomJoin.ChatRoomJoinRepository;
import com.allchat.allchat.domain.chatRoomJoin.RoleType;
import com.allchat.allchat.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final ChatRoomJoinRepository chatRoomJoinRepository;

    @Transactional
    public ChatRoom create(String title, Long principalId){

        ChatRoom chatRoom = ChatRoom.builder()
                .title(title)
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
