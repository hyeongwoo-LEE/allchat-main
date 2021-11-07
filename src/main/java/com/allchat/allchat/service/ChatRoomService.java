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
     * 채팅방 삭제
     */
    @Transactional
    public void remove(Long chatRoomId){

        chatRoomRepository.deleteById(chatRoomId);
    }

    /**
     * 전체 채팅방 목록
     */
    @Transactional(readOnly = true)
    public List<ChatRoomResDTO> getAllChatRoomList(Long principalId){

        List<ChatRoom> allChatRoomList = chatRoomRepository.findAll();

        List<ChatRoomResDTO> chatRoomResDTOList = allChatRoomList.stream().map(chatRoom -> {

            //참여 상태 체크
            for (ChatRoomJoin chatRoomJoin : chatRoom.getParticipantList()) {
                if (chatRoomJoin.getUser().getUserId().equals(principalId)) {

                    return chatRoom.toDTO(chatRoom.getParticipantList().size(), true);
                }
            }

            return chatRoom.toDTO(chatRoom.getParticipantList().size(), false);



        }).collect(Collectors.toList());

        return chatRoomResDTOList;
    }

    /**
     * 참여중인 채팅방 목록
     */
    @Transactional(readOnly = true)
    public List<ChatRoomResDTO> getJoinChatRoomList(Long principalId){

        //내가 참여한 채팅방 리스트
        List<ChatRoom> joinChatRoomList = chatRoomRepository.getJoinChatRoomList(principalId);

        List<ChatRoomResDTO> chatRoomResDTOList =
                joinChatRoomList.stream().map(chatRoom ->
                        chatRoom.toDTO(chatRoom.getParticipantList().size(), true))
                        .collect(Collectors.toList());

        return chatRoomResDTOList;
    }


}
