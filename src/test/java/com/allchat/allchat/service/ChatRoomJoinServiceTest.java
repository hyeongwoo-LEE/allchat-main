package com.allchat.allchat.service;

import com.allchat.allchat.domain.chatRoom.ChatRoom;
import com.allchat.allchat.domain.chatRoom.ChatRoomRepository;
import com.allchat.allchat.domain.chatRoomJoin.ChatRoomJoin;
import com.allchat.allchat.domain.chatRoomJoin.ChatRoomJoinRepository;
import com.allchat.allchat.domain.chatRoomJoin.RoleType;
import com.allchat.allchat.domain.user.User;
import com.allchat.allchat.domain.user.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class ChatRoomJoinServiceTest {

    @Autowired ChatRoomJoinService chatRoomJoinService;
    @Autowired UserRepository userRepository;
    @Autowired ChatRoomRepository chatRoomRepository;

    @Test
    void 방참여() throws Exception{
        //given
        User userA = createUser("userA");

        ChatRoom chatRoomA = createChatRoom("제목입니다.");

        //when
        ChatRoomJoin chatRoomJoin = chatRoomJoinService.join(chatRoomA.getChatRoomId(), userA.getUserId());

        //then
        Assertions.assertThat(chatRoomJoin.getJoinId()).isNotNull();
        Assertions.assertThat(chatRoomJoin.getRole()).isEqualTo(RoleType.GUEST);
        Assertions.assertThat(chatRoomJoin.getUser().getUserId()).isEqualTo(userA.getUserId());
        Assertions.assertThat(chatRoomJoin.getChatRoom().getChatRoomId()).isEqualTo(chatRoomA.getChatRoomId());
    }

    private ChatRoom createChatRoom(String title) {

        ChatRoom chatRoom = ChatRoom.builder()
                .title(title)
                .build();

        chatRoomRepository.save(chatRoom);

        return chatRoom;
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