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
public class ChatRoomJoinService {

    private final ChatRoomJoinRepository chatRoomJoinRepository;
    private final ChatRoomRepository chatRoomRepository;

    /**
     * 채팅방 참여
     */
    @Transactional
    public ChatRoomJoin join(Long chatRoomId, Long principalId){

        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId).orElseThrow(() ->
                new IllegalStateException("채팅방이 존재하지 않습니다.."));

        ChatRoomJoin chatRoomJoin = ChatRoomJoin.builder()
                .chatRoom(ChatRoom.builder().chatRoomId(chatRoomId).build())
                .user(User.builder().userId(principalId).build())
                .role(RoleType.GUEST)
                .build();

        chatRoomJoin.setChatRoom(chatRoom);

        chatRoomJoinRepository.save(chatRoomJoin);

        return chatRoomJoin;
    }

    /**
     * 채팅방 나가기
     */
    @Transactional
    public void remove(Long chatRoomId, Long principalId){
        chatRoomJoinRepository.delete(chatRoomId, principalId);
    }
}
