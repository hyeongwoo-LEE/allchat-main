package com.allchat.allchat.domain.chatRoomJoin;

import com.allchat.allchat.domain.chatRoom.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ChatRoomJoinRepository extends JpaRepository<ChatRoomJoin, Long> {

    ChatRoomJoin findByChatRoom(ChatRoom chatRoom);

    @Modifying(clearAutomatically = true)
    @Query("delete from ChatRoomJoin j " +
            "where j.chatRoom.chatRoomId=:chatRoomId AND j.user.userId=:userId")
    void delete(Long chatRoomId, Long userId);

}
