package com.allchat.allchat.domain.chatRoomJoin;

import com.allchat.allchat.domain.chatRoom.Room;
import com.allchat.allchat.domain.user.User;
import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@ToString(exclude = {"user","room"})
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
    @JoinColumn(name = "room_id", nullable = false)
    private Room room;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoleType role;

}
