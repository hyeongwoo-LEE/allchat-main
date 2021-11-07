package com.allchat.allchat.domain.chatRoom;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChatRoomRepository extends JpaRepository<ChatRoom, Long> {

    @Query("select cr from ChatRoom cr " +
            "join ChatRoomJoin crh on crh.chatRoom.chatRoomId = cr.chatRoomId " +
            "where crh.user.userId = :userId")
    List<ChatRoom> getJoinChatRoomList(Long userId);

}
