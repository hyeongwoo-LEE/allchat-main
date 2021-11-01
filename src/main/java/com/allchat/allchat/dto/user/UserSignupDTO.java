package com.allchat.allchat.dto.user;

import com.allchat.allchat.domain.user.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserSignupDTO {

    @NotBlank
    private String username;

    @NotBlank
    private String password;

    public User toEntity(Boolean isFromSocial){

        User user = User.builder()
                .username(username)
                .password(password)
                .isFromSocial(isFromSocial)
                .build();

        return user;
    }

}
