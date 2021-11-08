package com.allchat.allchat.domain.chatRoomJoin;

import com.allchat.allchat.domain.chatRoom.ChatRoom;
import com.allchat.allchat.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChatRoomJoinRepository extends JpaRepository<ChatRoomJoin, Long> {

    ChatRoomJoin findByChatRoom(ChatRoom chatRoom);

    @Modifying(clearAutomatically = true)
    @Query("delete from ChatRoomJoin j " +
            "where j.chatRoom.chatRoomId=:chatRoomId AND j.user.userId=:userId")
    void delete(Long chatRoomId, Long userId);

    @Query("select crj, u from ChatRoomJoin crj " +
            "left join User u on u.userId = crj.user.userId " +
            "where crj.chatRoom.chatRoomId = :chatRoomId")
    List<Object[]> getParticipantList(Long chatRoomId);

    boolean existsByChatRoomAndUser(ChatRoom chatRoom, User user);
}
