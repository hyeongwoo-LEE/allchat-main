package com.allchat.allchat.domain.chatRoom;


import com.allchat.allchat.domain.BaseEntity;
import lombok.*;
import org.springframework.stereotype.Component;

import javax.persistence.*;

@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ChatRoom extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatRoomId;

    @Column(nullable = false)
    private String title;

}
