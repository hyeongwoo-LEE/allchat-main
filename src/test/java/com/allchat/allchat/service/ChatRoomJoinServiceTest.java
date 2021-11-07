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

import java.util.NoSuchElementException;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class ChatRoomJoinServiceTest {

    @Autowired ChatRoomJoinService chatRoomJoinService;
    @Autowired UserRepository userRepository;
    @Autowired ChatRoomRepository chatRoomRepository;
    @Autowired ChatRoomJoinRepository chatRoomJoinRepository;

    @Test
    void 방참여() throws Exception{
        //given
        //방장 생성
        User master = createUser("master");
        //회원 생성
        User userA = createUser("userA");

        ChatRoom chatRoomA = createChatRoom(master,"제목입니다.");

        //when
        ChatRoomJoin chatRoomJoin = chatRoomJoinService.join(chatRoomA.getChatRoomId(), userA.getUserId());

        //then
        Assertions.assertThat(chatRoomJoin.getJoinId()).isNotNull();
        Assertions.assertThat(chatRoomJoin.getChatRoom()).isEqualTo(chatRoomA);
        Assertions.assertThat(chatRoomJoin.getRole()).isEqualTo(RoleType.GUEST);
        Assertions.assertThat(chatRoomJoin.getUser().getUserId()).isEqualTo(userA.getUserId());
        Assertions.assertThat(chatRoomJoin.getChatRoom().getChatRoomId()).isEqualTo(chatRoomA.getChatRoomId());
    }

    @Test
    void 방_나가기() throws Exception{
        //given
        //방장 생성
        User master = createUser("master");
        //회원 생성
        User userA = createUser("userA");

        //방 생성
        ChatRoom chatRoomA = createChatRoom(master,"제목입니다.");

        //방 참여 생성
        ChatRoomJoin chatRoomJoin = createChatRoomJoin(chatRoomA, userA);

        //when
        chatRoomJoinService.remove(chatRoomA.getChatRoomId(), userA.getUserId());

        //then
        NoSuchElementException e = assertThrows(NoSuchElementException.class,
                () -> chatRoomJoinRepository.findById(chatRoomJoin.getJoinId()).get());

        Assertions.assertThat(e.getMessage()).isEqualTo("No value present");

    }

    private ChatRoomJoin createChatRoomJoin(ChatRoom chatRoom, User user) {

        ChatRoomJoin chatRoomJoin = ChatRoomJoin.builder()
                .user(user)
                .chatRoom(chatRoom)
                .role(RoleType.GUEST)
                .build();

        chatRoomJoinRepository.save(chatRoomJoin);

        return chatRoomJoin;
    }

    private ChatRoom createChatRoom(User master, String title) {

        ChatRoom chatRoom = ChatRoom.builder()
                .user(master)
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