package com.allchat.allchat.service;

import com.allchat.allchat.domain.chatRoom.ChatRoom;
import com.allchat.allchat.domain.chatRoom.ChatRoomRepository;
import com.allchat.allchat.domain.chatRoomJoin.ChatRoomJoin;
import com.allchat.allchat.domain.chatRoomJoin.ChatRoomJoinRepository;
import com.allchat.allchat.domain.chatRoomJoin.RoleType;
import com.allchat.allchat.domain.user.User;
import com.allchat.allchat.domain.user.UserRepository;
import com.allchat.allchat.dto.chatRoom.ChatRoomDTO;
import com.allchat.allchat.dto.chatRoom.ChatRoomResDTO;
import com.fasterxml.jackson.annotation.JacksonAnnotationsInside;
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
class ChatRoomServiceTest {
/*
    @Autowired ChatRoomService chatRoomService;
    @Autowired UserRepository userRepository;
    @Autowired ChatRoomJoinRepository chatRoomJoinRepository;
    @Autowired ChatRoomRepository chatRoomRepository;

    @Test
    void 방생성() throws Exception{
        //given
        User userA = createUser("userA");

        ChatRoomDTO chatRoomDTO = ChatRoomDTO.builder()
                .masterId(userA.getUserId())
                .title("다드루와")
                .build();

        //when
        ChatRoom chatRoom = chatRoomService.create(chatRoomDTO, userA.getUserId());

        //then
        //ChatRoom 검증
        Assertions.assertThat(chatRoom.getChatRoomId()).isNotNull();
        Assertions.assertThat(chatRoom.getUser().getUserId()).isEqualTo(chatRoomDTO.getMasterId());
        Assertions.assertThat(chatRoom.getTitle()).isEqualTo(chatRoomDTO.getTitle());

        //chatRoomJoin 검증
        ChatRoomJoin chatRoomJoin = chatRoomJoinRepository.findByChatRoom(chatRoom);

        Assertions.assertThat(chatRoomJoin.getChatRoom().getChatRoomId()).isEqualTo(chatRoom.getChatRoomId());
        Assertions.assertThat(chatRoomJoin.getUser().getUserId()).isEqualTo(userA.getUserId());
        Assertions.assertThat(chatRoomJoin.getRole()).isEqualTo(RoleType.MASTER);
    }

    @Test
    void 방삭제() throws Exception{
        //given
        //방장생성
        User master = createUser("master");

        //채팅방 생성 - 참여자 리스트에 방장 추가
        ChatRoom chatRoom = createChatRoom(master, "아무나 들어와");

        //참여자 생성
        User user = createUser("user");

        //참여
        ChatRoomJoin chatRoomJoin = ChatRoomJoin.builder()
                .user(user)
                .role(RoleType.GUEST)
                .build();

        chatRoomJoin.setChatRoom(chatRoom);
        chatRoomJoinRepository.save(chatRoomJoin);

        //when
        chatRoomService.remove(chatRoom.getChatRoomId());

        //then
        //삭제된 채팅방의 참여리스트 검색
        NoSuchElementException e1 = assertThrows(NoSuchElementException.class,
                () -> chatRoomJoinRepository.findById(chatRoomJoin.getJoinId()).get());

        //삭제된 채팅방 검색
        NoSuchElementException e2 = assertThrows(NoSuchElementException.class,
                () -> chatRoomRepository.findById(chatRoom.getChatRoomId()).get());

        Assertions.assertThat(e1.getMessage()).isEqualTo("No value present");
        Assertions.assertThat(e2.getMessage()).isEqualTo("No value present");
    }

    @Test
    void 전체_채팅방리스트() throws Exception{
        //given

        //채팅방 3개 생성
        IntStream.rangeClosed(1,3).forEach(i -> {
            User user = createUser("user" + i);
            createChatRoom(user, "채팅방" + i);
        });

        List<ChatRoom> chatRoomList = chatRoomRepository.findAll();
        ChatRoom chatRoom1 = chatRoomList.get(0);
        ChatRoom chatRoom2 = chatRoomList.get(1);
        ChatRoom chatRoom3 = chatRoomList.get(2);

        //chatRoom1 방장이 chatRoom2에 참여
        ChatRoomJoin chatRoomJoinA = ChatRoomJoin.builder()
                .user(chatRoom1.getUser())
                .role(RoleType.GUEST)
                .build();

        chatRoomJoinA.setChatRoom(chatRoom2);

        chatRoomJoinRepository.save(chatRoomJoinA);

        //현재 사용 유저 생성
        User currentUser = createUser("CurrentUser");

        ChatRoomJoin chatRoomJoinB = ChatRoomJoin.builder()
                .user(currentUser)
                .role(RoleType.GUEST)
                .build();

        chatRoomJoinB.setChatRoom(chatRoom1);
        chatRoomJoinRepository.save(chatRoomJoinB);

        /**
         * chatRoom1 - 참여인원 2 (현재사용유저 포함 - participantState - true)
         * chatRoom2 - 참여인원 2
         * chatRoom3 - 참여인원 1
         */

    /*
        //when
        List<ChatRoomResDTO> result = chatRoomService.getAllChatRoomList(currentUser.getUserId());

        //then
        for(ChatRoomResDTO chatRoomResDTO : result){
            System.out.println(chatRoomResDTO);
        }

        Assertions.assertThat(result.size()).isEqualTo(3);

    }

    /*
    @Test
    void test() throws Exception{
        //given

        //when
        List<ChatRoomResDTO> allChatRoomList = chatRoomService.getAllChatRoomList(34L);

        //then
        for(ChatRoomResDTO chatRoomResDTO : allChatRoomList){
            System.out.println(chatRoomResDTO);
        }
    }*/
/*
    @Test
    void 참여중_채티방리스트() throws Exception{
        //given
        //채팅방 3개 생성
        IntStream.rangeClosed(1,3).forEach(i -> {
            User user = createUser("user" + i);
            createChatRoom(user, "채팅방" + i);
        });

        List<ChatRoom> chatRoomList = chatRoomRepository.findAll();
        ChatRoom chatRoom1 = chatRoomList.get(0);
        ChatRoom chatRoom2 = chatRoomList.get(1);
        ChatRoom chatRoom3 = chatRoomList.get(2);

        //chatRoom1 방장이 chatRoom2에 참여
        ChatRoomJoin chatRoomJoinA = ChatRoomJoin.builder()
                .user(chatRoom1.getUser())
                .role(RoleType.GUEST)
                .build();

        chatRoomJoinA.setChatRoom(chatRoom2);

        chatRoomJoinRepository.save(chatRoomJoinA);

        //현재 사용 유저 생성
        User currentUser = createUser("CurrentUser");

        //chatRoom1 에 참여
        ChatRoomJoin chatRoomJoinB = ChatRoomJoin.builder()
                .user(currentUser)
                .role(RoleType.GUEST)
                .build();

        chatRoomJoinB.setChatRoom(chatRoom1);
        chatRoomJoinRepository.save(chatRoomJoinB);

        //chatRoom2 에 참여
        ChatRoomJoin chatRoomJoinC = ChatRoomJoin.builder()
                .user(currentUser)
                .role(RoleType.GUEST)
                .build();

        chatRoomJoinC.setChatRoom(chatRoom2);
        chatRoomJoinRepository.save(chatRoomJoinC);

        /**
         * chatRoom1 - 참여인원 2 (현재사용유저 포함 - participantState - true)
         * chatRoom2 - 참여인원 3 (현재사용유저 포함 - participantState - true)
         * chatRoom3 - 참여인원 1
         */
/*
        //when
        List<ChatRoomResDTO> result = chatRoomService.getJoinChatRoomList(currentUser.getUserId());

        //then
        for (ChatRoomResDTO chatRoomResDTO : result){
            System.out.println(chatRoomResDTO);
        }

        Assertions.assertThat(result.size()).isEqualTo(2);
    }

    /*
    @Test
    void test2() throws Exception{
        //given

        //when
        List<ChatRoomResDTO> result = chatRoomService.getJoinChatRoomList(8L);

        //then
        for(ChatRoomResDTO chatRoomResDTO : result){
            System.out.println(chatRoomResDTO);
        }
    }*/
/*
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

*/


}