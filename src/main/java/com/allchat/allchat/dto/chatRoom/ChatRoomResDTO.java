package com.allchat.allchat.dto.chatRoom;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoomResDTO {

    private Long chatRoomId;

    private Long masterId;

    private String title;

    private int participantCount;

    private Boolean participantState;
}
