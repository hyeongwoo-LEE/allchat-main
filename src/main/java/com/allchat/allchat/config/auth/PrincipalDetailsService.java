package com.allchat.allchat.config.auth;

import com.allchat.allchat.domain.user.User;
import com.allchat.allchat.domain.user.UserRepository;
import com.allchat.allchat.handler.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class PrincipalDetailsService implements UserDetailsService {

    private final UserRepository userRepository;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUsername(username).orElseThrow(() ->
                new CustomException("존재하지 않은 아이디입니다."));

        return new PrincipalDetails(user);
    }
}
