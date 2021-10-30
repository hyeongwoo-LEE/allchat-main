package com.allchat.allchat.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.impl.DefaultClaims;
import io.jsonwebtoken.impl.DefaultJws;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;

import java.io.UnsupportedEncodingException;
import java.security.Key;
import java.time.ZonedDateTime;
import java.util.Date;

@Log4j2
public class JwtUtil implements InitializingBean {

    @Value("${jwt.secretKey}")
    private String secretKey;

    private long expire = 60 * 24 * 30;

    private Key key;

    @Override
    public void afterPropertiesSet() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    //토큰 생성
    public String generateToken(Long userId) throws UnsupportedEncodingException {

        return "Bearer " + Jwts.builder()
                .setIssuedAt(new Date())
                .setExpiration(Date.from(ZonedDateTime.now().plusMinutes(expire).toInstant()))
                .claim("userId", userId)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public Long validateAndExtract(String tokenStr){

        Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(tokenStr).getBody();
        return null;

    }
    
    

}
