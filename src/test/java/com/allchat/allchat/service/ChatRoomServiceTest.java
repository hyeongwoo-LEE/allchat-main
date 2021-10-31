package com.allchat.allchat.service;

import com.allchat.allchat.domain.chatRoom.ChatRoom;
import com.allchat.allchat.domain.chatRoomJoin.ChatRoomJoin;
import com.allchat.allchat.domain.chatRoomJoin.ChatRoomJoinRepository;
import com.allchat.allchat.domain.chatRoomJoin.RoleType;
import com.allchat.allchat.domain.user.User;
import com.allchat.allchat.domain.user.UserRepository;
import com.allchat.allchat.dto.chatRoom.ChatRoomDTO;
import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class ChatRoomServiceTest {

    @Autowired ChatRoomService chatRoomService;
    @Autowired UserRepository userRepository;
    @Autowired ChatRoomJoinRepository chatRoomJoinRepository;

    @Test
    void 방생성() throws Exception{
        //given
        User userA = createUser("userA");

        ChatRoomDTO chatRoomDTO = ChatRoomDTO.builder()
                .title("다드루와")
                .build();

        //when
        ChatRoom chatRoom = chatRoomService.create(chatRoomDTO.getTitle(), userA.getUserId());

        //then
        //ChatRoom 검증
        Assertions.assertThat(chatRoom.getChatRoomId()).isNotNull();
        Assertions.assertThat(chatRoom.getTitle()).isEqualTo(chatRoomDTO.getTitle());

        //chatRoomJoin 검증
        ChatRoomJoin chatRoomJoin = chatRoomJoinRepository.findByChatRoom(chatRoom);

        Assertions.assertThat(chatRoomJoin.getChatRoom().getChatRoomId()).isEqualTo(chatRoom.getChatRoomId());
        Assertions.assertThat(chatRoomJoin.getUser().getUserId()).isEqualTo(userA.getUserId());
        Assertions.assertThat(chatRoomJoin.getRole()).isEqualTo(RoleType.MASTER);


    }

    private User createUser(String username) {

        User user = User.builder()
                .username(username)
                .password("1111")
                .isFromSocial(false)
                .build();

        userRepository.save(user);

        return user;
    }




}