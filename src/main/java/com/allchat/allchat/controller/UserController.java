package com.allchat.allchat.controller;

import com.allchat.allchat.config.auth.PrincipalDetails;
import com.allchat.allchat.domain.user.UserRepository;
import com.allchat.allchat.dto.CMRespDTO;
import com.allchat.allchat.dto.login.LoginResDTO;
import com.allchat.allchat.dto.user.UserSignupDTO;
import com.allchat.allchat.jwt.JwtUtil;
import com.allchat.allchat.kakao.KakaoProfile;
import com.allchat.allchat.kakao.OAuthToken;
import com.allchat.allchat.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;

@Log4j2
@RequiredArgsConstructor
@RestController
public class UserController {

    @Value("{cosKey}")
    private String cosKey;

    private final UserRepository userRepository;
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @GetMapping("/auth/kakao/callback")
    public ResponseEntity<?> kakaoCallback(@RequestParam("code") String code) throws JsonProcessingException, UnsupportedEncodingException {

        //POST 방식으로 key=value 데이터를 요청 (카카오쪽으로)
        RestTemplate rt = new RestTemplate();

        //HttpHeader 오브젝트 생성 (엔티티) - 헤더, 바디
        HttpHeaders headers = new HttpHeaders();

        headers.add("Content-type","application/x-www-form-urlencoded;charset=utf-8");

        //HttpBody 오브젝트 생성
        MultiValueMap<String,String> params = new LinkedMultiValueMap<>();

        params.add("grant_type", "authorization_code");
        params.add("client_id", "3e716bc2780a7b5fe1da319c4487c6f9");
        params.add("redirect_uri", "http://localhost:9090/auth/kakao/callback");
        params.add("code", code);

        //HttpHeader와 HttpBody를 하나의 오브젝트에 담기
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(params, headers);

        //Http요청하기 - Post방식으로 -그리고 response 변수의 응답 받음.
        ResponseEntity<String> response = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class);

        ObjectMapper objectMapper = new ObjectMapper();

        OAuthToken oAuthToken = objectMapper.readValue(response.getBody(), OAuthToken.class);

        System.out.println(oAuthToken.getAccess_token());

        RestTemplate rt2 = new RestTemplate();

        HttpHeaders headers2 = new HttpHeaders();

        headers2.add("Content-type","application/x-www-form-urlencoded;charset=utf-8");
        headers2.add("Authorization", "Bearer " + oAuthToken.getAccess_token());


        HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest2 = new HttpEntity<>(headers2);

        ResponseEntity<String> response2 = rt2.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoProfileRequest2,
                String.class);

        ObjectMapper objectMapper2 = new ObjectMapper();

        KakaoProfile kakaoProfile = objectMapper2.readValue(response2.getBody(), KakaoProfile.class);

        UserSignupDTO kakaoUserSignupDTO = UserSignupDTO.builder()
                .username(kakaoProfile.getProperties().getNickname() + " kakao")
                .password(cosKey)
                .build();

        if(userRepository.findByUsername(kakaoUserSignupDTO.getUsername()).isEmpty()){
            log.info("신입 회원입니다.");
            userService.kakaoJoin(kakaoUserSignupDTO);
        }


        //로그인 처리
        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(kakaoUserSignupDTO.getUsername(), cosKey));

        SecurityContextHolder.getContext().setAuthentication(authenticate);

        PrincipalDetails principalDetails = (PrincipalDetails) authenticate.getPrincipal();

        String jwtToken = jwtUtil.generateToken(principalDetails.getUser().getUserId());

        LoginResDTO loginResDTO = LoginResDTO.builder()
                .userId(principalDetails.getUser().getUserId())
                .jwtToken(jwtToken)
                .build();

        return new ResponseEntity<>(new CMRespDTO<>(1, "카카오 로그인 성공", loginResDTO), HttpStatus.OK);
    }
}
