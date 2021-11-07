package com.allchat.allchat.domain.chatRoomJoin;

import com.allchat.allchat.domain.chatRoom.ChatRoom;
import com.allchat.allchat.domain.user.User;
import com.allchat.allchat.dto.chatRoomJoin.ChatRoomJoinResDTO;
import lombok.*;

import javax.persistence.*;

@Getter
@ToString(exclude = {"user","chatRoom"})
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ChatRoomJoin {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long joinId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id", nullable = false)
    private ChatRoom chatRoom;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoleType role;

    //-----연관관계 메서드-------
    public void setChatRoom(ChatRoom chatRoom){
        this.chatRoom = chatRoom;
        chatRoom.getParticipantList().add(this);
    }

}
