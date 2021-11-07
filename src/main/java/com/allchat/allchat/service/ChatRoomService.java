package com.allchat.allchat.service;

import com.allchat.allchat.domain.chatRoom.ChatRoom;
import com.allchat.allchat.domain.chatRoom.ChatRoomRepository;
import com.allchat.allchat.domain.chatRoomJoin.ChatRoomJoin;
import com.allchat.allchat.domain.chatRoomJoin.ChatRoomJoinRepository;
import com.allchat.allchat.domain.chatRoomJoin.RoleType;
import com.allchat.allchat.domain.user.User;
import com.allchat.allchat.dto.chatRoom.ChatRoomDTO;
import com.allchat.allchat.dto.chatRoom.ChatRoomResDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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
                .role(RoleType.MASTER)
                .build();

        chatRoomJoin.setChatRoom(chatRoom);

        chatRoomJoinRepository.save(chatRoomJoin);

        return chatRoom;
    }

    /**
     * 전체 채팅방 목록
     */
    @Transactional
    public List<ChatRoomResDTO> getAllChatRoomList(Long principalId){

        List<ChatRoom> allChatRoomList = chatRoomRepository.findAll();

        List<ChatRoomResDTO> chatRoomResDTOList = allChatRoomList.stream().map(chatRoom -> {

            System.out.println("-------------조회 시작--------------");
            //참여 상태 체크
            for (ChatRoomJoin chatRoomJoin : chatRoom.getParticipantList()) {
                if (chatRoomJoin.getUser().getUserId().equals(principalId)) {
                    System.out.println("-------------조회 종료--------------");
                    return chatRoom.toDTO(chatRoom.getParticipantList().size(), true);
                }
            }
            System.out.println("-------------조회 종료--------------");
            ChatRoomResDTO result = chatRoom.toDTO(chatRoom.getParticipantList().size(), false);
            return result;


        }).collect(Collectors.toList());

        return chatRoomResDTOList;
    }


}
