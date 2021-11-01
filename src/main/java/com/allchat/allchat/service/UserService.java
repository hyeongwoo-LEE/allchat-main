package com.allchat.allchat.service;

import com.allchat.allchat.domain.user.User;
import com.allchat.allchat.domain.user.UserRepository;
import com.allchat.allchat.dto.user.UserDTO;
import com.allchat.allchat.dto.user.UserSignupDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    /**
     * 아이디 중복확인
     */
    @Transactional(readOnly = true)
    public Boolean isDuplicateNickname(String username) {

        if(userRepository.findByUsername(username).isPresent()){
            return true;
        }else{
            return false;
        }
    }

    /**
     * 회원가입
     */
    @Transactional
    public User join(UserSignupDTO userSignupDTO){

        if(userRepository.findByUsername(userSignupDTO.getUsername()).isPresent()){
            throw new IllegalStateException("이미 존재하는 아이디입니다.");
        }

        String rawPassword = userSignupDTO.getPassword();
        String encPassword = passwordEncoder.encode(rawPassword);

        userSignupDTO.setPassword(encPassword);

        User user = userRepository.save(userSignupDTO.toEntity(false));

        return user;
    }

    /**
     * 카카오 회원가입
     */
    public User kakaoJoin(UserSignupDTO userSignupDTO){

        String rawPassword = userSignupDTO.getPassword();
        String encPassword = passwordEncoder.encode(rawPassword);

        userSignupDTO.setPassword(encPassword);

        User user = userRepository.save(userSignupDTO.toEntity(true));

        return user;
    }


}
