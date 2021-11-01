package com.allchat.allchat.config;

import com.allchat.allchat.config.auth.oauth.OAuth2DetailsService;
import com.allchat.allchat.domain.user.UserRepository;
import com.allchat.allchat.filter.JwtAuthenticationEntryPoint;
import com.allchat.allchat.filter.JwtAuthorizationFilter;
import com.allchat.allchat.filter.LoginFilter;
import com.allchat.allchat.jwt.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@RequiredArgsConstructor
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private final UserRepository userRepository;
    private final OAuth2DetailsService oAuth2DetailsService;

    @Bean
    public PasswordEncoder encode(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtUtil jwtUtil(){
        return new JwtUtil();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        //세션 사용 x
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        http.formLogin().disable();
        http.httpBasic().disable();
        http.csrf().disable(); //세션 사용 x

        http.authorizeRequests()
                .antMatchers("/auth/**").permitAll()
                .anyRequest().authenticated(); // /auth/** 이외 모두 인증 필수.

        http.addFilter(new LoginFilter(authenticationManager(), jwtUtil()))
                .addFilter(new JwtAuthorizationFilter(authenticationManager(), jwtUtil(), userRepository));

        http.exceptionHandling()
                .authenticationEntryPoint(jwtAuthenticationEntryPoint);

        http.oauth2Login()
                .userInfoEndpoint().userService(oAuth2DetailsService)
                .and()
//                .successHandler()
//                .failureHandler()
                .permitAll();


    }
}
