package com.allchat.allchat.service;

import com.allchat.allchat.domain.chatRoom.ChatRoom;
import com.allchat.allchat.domain.chatRoom.ChatRoomRepository;
import com.allchat.allchat.domain.chatRoomJoin.ChatRoomJoin;
import com.allchat.allchat.domain.chatRoomJoin.ChatRoomJoinRepository;
import com.allchat.allchat.domain.chatRoomJoin.RoleType;
import com.allchat.allchat.domain.user.User;
import com.allchat.allchat.domain.user.UserRepository;
import com.allchat.allchat.dto.chatRoomJoin.ChatRoomJoinResDTO;
import com.allchat.allchat.dto.chatRoomJoin.ChatRoomJoinTimeDTO;
import com.allchat.allchat.handler.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Transactional
@RequiredArgsConstructor
@Service
public class ChatRoomJoinService {

    private final ChatRoomJoinRepository chatRoomJoinRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final UserRepository userRepository;

    /**
     * 채팅방 참여
     */
    @Transactional
    public ChatRoomJoin join(Long chatRoomId, Long principalId){

        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId).orElseThrow(() ->
                new CustomException("채팅방이 존재하지 않습니다.."));

        User user = userRepository.findById(principalId).orElseThrow(() ->
                new CustomException("존재하는 회원이 아닙니다."));

        if(chatRoomJoinRepository.existsByChatRoomAndUser(chatRoom, user)){
            throw new CustomException("이미 참여하고 있는 채팅방입니다.");
        }

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

    /**
     * 채팅방 참여자 리스트
     */
    @Transactional(readOnly = true)
    public List<ChatRoomJoinResDTO> getParticipantList(Long chatRoomId){

        List<Object[]> result = chatRoomJoinRepository.getParticipantList(chatRoomId);

        List<ChatRoomJoinResDTO> chatRoomJoinResDTOList =
                result.stream().map(obj ->
                        entityToDTO((ChatRoomJoin) obj[0], (User) obj[1])).collect(Collectors.toList());

        return chatRoomJoinResDTOList;
    }

    /**
     * 채팅방 입장 시간 조회
     */
    @Transactional(readOnly = true)
    public ChatRoomJoinTimeDTO getJoinTime(Long roomId, Long principalId){

        ChatRoomJoin chatRoomJoin = chatRoomJoinRepository.findByChatRoomIdAndUserId(roomId, principalId);

        return chatRoomJoin.toJoinTimeDTO();

    }

    private ChatRoomJoinResDTO entityToDTO(ChatRoomJoin chatRoomJoin, User user) {

        ChatRoomJoinResDTO chatRoomJoinResDTO = ChatRoomJoinResDTO.builder()
                .joinId(chatRoomJoin.getJoinId())
                .userId(user.getUserId())
                .username(user.getUsername())
                .build();

        return chatRoomJoinResDTO;

    }
}
