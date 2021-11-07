package com.allchat.allchat.domain.chatRoom;
import com.allchat.allchat.domain.BaseEntity;
import com.allchat.allchat.domain.chatRoomJoin.ChatRoomJoin;
import com.allchat.allchat.domain.user.User;
import com.allchat.allchat.dto.chatRoom.ChatRoomResDTO;
import lombok.*;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ChatRoom extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long chatRoomId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "master_id", nullable = false)
    private User user;

    @BatchSize(size = 36) //처음 지연 로딩시 36 레코드를 한번에 가져옴.
    @Builder.Default
    @OneToMany(mappedBy = "chatRoom", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE)
    private List<ChatRoomJoin> participantList = new ArrayList<>();


    @Column(nullable = false)
    private String title;

    public ChatRoomResDTO toDTO(int participantCount, Boolean participantState){

        ChatRoomResDTO chatRoomResDTO = ChatRoomResDTO.builder()
                .chatRoomId(chatRoomId)
                .masterId(user.getUserId())
                .title(title)
                .participantCount(participantCount)
                .participantState(participantState)
                .build();

        return chatRoomResDTO;
    }
}
