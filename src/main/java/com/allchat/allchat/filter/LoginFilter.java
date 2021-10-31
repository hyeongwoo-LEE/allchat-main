package com.allchat.allchat.filter;

import com.allchat.allchat.domain.user.User;
import com.allchat.allchat.dto.CMRespDTO;
import com.allchat.allchat.dto.user.UserDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

//login 시 작동 - 스프링 시큐리티에서 제공
//formLong.disable 으로 설정시 작동 x -> 때문에 추가 securityConfig 에  필터 등록

@Log4j2
@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    private ObjectMapper objectMapper = new ObjectMapper();

    /**
     *  로그인 요청시 실행되는 함수
     */
    @SneakyThrows
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {

        System.out.println("로그인 시도");

        UserDTO userDTO = objectMapper.readValue(request.getInputStream(), UserDTO.class);

        log.info("UserDTO: " + userDTO);

        UsernamePasswordAuthenticationToken authenticationToken
                = new UsernamePasswordAuthenticationToken(userDTO.getUsername(), userDTO.getPassword());

        return authenticationManager.authenticate(authenticationToken);
    }

    /**
     *  로그인 성공시
     */
    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult)
            throws IOException, ServletException {

        System.out.println("성공!!!!!!!");

        //TODO jwt 발급
    }

    /**
     *  로그인 실패시
     */
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, AuthenticationException failed) throws IOException, ServletException {

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        response.setContentType("application/json;charset=utf-8");


        CMRespDTO<?> cmRespDTO = new CMRespDTO<>(-1, failed.getMessage(), null);

        String result = objectMapper.writeValueAsString(cmRespDTO);

        response.getWriter().write(result);
    }
}
