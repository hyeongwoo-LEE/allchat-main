package com.allchat.allchat.filter;

import com.allchat.allchat.config.auth.PrincipalDetails;
import com.allchat.allchat.domain.user.User;
import com.allchat.allchat.domain.user.UserRepository;
import com.allchat.allchat.jwt.JwtUtil;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//권한이나 인증이 일요한 특정 주소를 요청했을 때 위 필터를 무조건 타게 되었있음.
//만약에 권한 인증이 필요한 주소가 아니라면 이 필터를 안탐.
@Log4j2
public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    private JwtUtil jwtUtil;
    private UserRepository userRepository;


    public JwtAuthorizationFilter(AuthenticationManager authenticationManager,
                                  JwtUtil jwtUtil, UserRepository userRepository) {
        super(authenticationManager);
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
    }

    @Transactional
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {

        String authHeader = request.getHeader("Authorization");

        System.out.println("url:" + request.getRequestURI());
        System.out.println("Authorization: " + authHeader);

        if (StringUtils.hasText(authHeader) && authHeader.startsWith("Bearer ")) {
            //jwt 토큰 검증
            Long userId = jwtUtil.validateAndExtract(authHeader.substring(7));

            log.info("userId: " + userId);

            if(userId != null){

                User user = userRepository.findById(userId).orElseThrow(() -> {
                    throw new IllegalStateException("존재하지 않은 회원입니다.");
                });

                PrincipalDetails principalDetails = new PrincipalDetails(user);

                //강제로 Authentication 만들기
                Authentication authentication = new UsernamePasswordAuthenticationToken(principalDetails, null, principalDetails.getAuthorities());

                //강제로 시큐리티의 세션에 접근하여 Authentication 객체를 저장.
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        chain.doFilter(request,response);
    }

}
