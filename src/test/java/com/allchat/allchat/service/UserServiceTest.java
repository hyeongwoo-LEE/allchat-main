package com.allchat.allchat.service;

import com.allchat.allchat.domain.user.User;
import com.allchat.allchat.domain.user.UserRepository;
import com.allchat.allchat.dto.user.UserSignupDTO;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
class UserServiceTest {

    @Autowired UserService userService;
    @Autowired UserRepository userRepository;
    @Autowired PasswordEncoder passwordEncoder;
    
    @Test
    void 아이디_중복확인_존재o() throws Exception{
        //given
        User userA = createUser("userA");

        //when
        Boolean result = userService.isDuplicateNickname(userA.getUsername());

        //then
        Assertions.assertThat(result).isTrue();
    }

    @Test
    void 아이디_중복확인_존재x() throws Exception{
        //given

        //when
        Boolean result = userService.isDuplicateNickname("사용가능 아이디");

        //then
        Assertions.assertThat(result).isFalse();
    }
    
    @Test
    void 회원가입() throws Exception{
        //given
        UserSignupDTO userSignupDTO = UserSignupDTO.builder()
                .username("userA")
                .password("1111")
                .build();

        String rawPassword = userSignupDTO.getPassword();

        //when
        User user = userService.join(userSignupDTO);

        //then
        Assertions.assertThat(user.getUserId()).isNotNull();
        Assertions.assertThat(user.getUsername()).isEqualTo(userSignupDTO.getUsername());
        Assertions.assertThat(user.getIsFromSocial()).isFalse();
        Assertions.assertThat(
                passwordEncoder.matches(rawPassword, user.getPassword())).isTrue();

    }

    private User createUser(String username) {

        User user = User.builder()
                .username(username)
                .password("1111")
                .isFromSocial(false)
                .build();

        userRepository.save(user);

        return user;
    }

}