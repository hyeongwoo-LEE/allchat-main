package com.allchat.allchat.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CMRespDTO<T> {
    private int Code; //1(성공), -1(실패)
    private String message;
    private T data;
}
