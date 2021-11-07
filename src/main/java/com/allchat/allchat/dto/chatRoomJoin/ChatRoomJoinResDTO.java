package com.allchat.allchat.dto.chatRoomJoin;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChatRoomJoinResDTO {

    private Long joinId;

    private Long userId;

    private String username;
}
