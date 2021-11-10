package com.allchat.allchat.service;

import com.allchat.allchat.domain.chatRoom.ChatRoom;
import com.allchat.allchat.domain.chatRoom.ChatRoomRepository;
import com.allchat.allchat.domain.chatRoomJoin.ChatRoomJoin;
import com.allchat.allchat.domain.chatRoomJoin.ChatRoomJoinRepository;
import com.allchat.allchat.domain.chatRoomJoin.RoleType;
import com.allchat.allchat.domain.user.User;
import com.allchat.allchat.domain.user.UserRepository;
import com.allchat.allchat.dto.chatRoom.ChatRoomDTO;
import com.allchat.allchat.dto.chatRoomJoin.ChatRoomJoinResDTO;
import com.allchat.allchat.dto.chatRoomJoin.ChatRoomJoinTimeDTO;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@SpringBootTest
class ChatRoomJoinServiceTest {

    @Autowired ChatRoomJoinService chatRoomJoinService;
    @Autowired UserRepository userRepository;
    @Autowired ChatRoomRepository chatRoomRepository;
    @Autowired ChatRoomJoinRepository chatRoomJoinRepository;
    @Autowired ChatRoomService chatRoomService;

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

    @Test
    void 채팅방_참여자_리스트() throws Exception{
        //given
        //방장생성
        User master = createUser("master");

        //채팅방 생성
        ChatRoom chatRoom = createChatRoom(master, "아무나 들어와");

        //채팅 참가 3명
        IntStream.rangeClosed(1,3).forEach(i -> {

            User user = createUser("user" + i);

            ChatRoomJoin chatRoomJoin = ChatRoomJoin.builder()
                    .user(user)
                    .role(RoleType.GUEST)
                    .build();

            chatRoomJoin.setChatRoom(chatRoom);
            chatRoomJoinRepository.save(chatRoomJoin);
        });

        //when
        List<ChatRoomJoinResDTO> result = chatRoomJoinService.getParticipantList(chatRoom.getChatRoomId());

        //then
        for (ChatRoomJoinResDTO chatRoomJoinResDTO : result){
            System.out.println(chatRoomJoinResDTO);
        }

        Assertions.assertThat(result.size()).isEqualTo(4);
    }

    @Test
    void 채팅방_입장시간_조회() throws Exception{
        //given
        //방장 생성
        User master = createUser("방장");

        //채팅방 생성
        ChatRoom chatRoom = createChatRoom(master, "들어오세요");

        //참여자 생성
        User user = createUser("participantUser");

        //chatRoomJoin 생성
        ChatRoomJoin chatRoomJoin = ChatRoomJoin.builder()
                .user(user)
                .role(RoleType.GUEST)
                .build();

        chatRoomJoin.setChatRoom(chatRoom);
        chatRoomJoinRepository.save(chatRoomJoin);



        //when
        ChatRoomJoinTimeDTO joinTimeDTO = chatRoomJoinService.getJoinTime(chatRoom.getChatRoomId(), user.getUserId());

        //then
        Assertions.assertThat(joinTimeDTO.getUsername()).isEqualTo(user.getUsername());
        Assertions.assertThat(joinTimeDTO.getJoinDateTime()).isEqualTo(chatRoomJoin.getRegDate());
    }

/*
    @Test
    void test() throws Exception{
        //given

        //when
        ChatRoomJoinTimeDTO joinTimeDTO = chatRoomJoinService.getJoinTime(2L, 4L);
        //then
        System.out.println(joinTimeDTO);
    }
*/
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

        ChatRoomDTO chatRoomDTO = ChatRoomDTO.builder()
                .masterId(master.getUserId())
                .title(title)
                .build();

        return chatRoomService.create(chatRoomDTO, master.getUserId());
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