package com.allchat.allchat.domain.chatRoomJoin;

import com.allchat.allchat.domain.chatRoom.ChatRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface ChatRoomJoinRepository extends JpaRepository<ChatRoomJoin, Long> {

    ChatRoomJoin findByChatRoom(ChatRoom chatRoom);


}
