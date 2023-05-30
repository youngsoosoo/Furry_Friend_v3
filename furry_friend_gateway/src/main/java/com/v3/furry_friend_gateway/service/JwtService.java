package com.v3.furry_friend_gateway.service;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.log4j.Log4j2;

import java.util.Base64;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.stereotype.Component;

@Component
@Log4j2
public class JwtService {

    // 안전한 256비트 이상의 키 생성
    private final SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    // 키를 문자열로 변환하여 저장 또는 사용할 수 있습니다.
    private final String key = Base64.getEncoder().encodeToString(secretKey.getEncoded());

    // 토큰의 유효성 검사 메서드
    public boolean validateToken(String token) {
        try {
            Map<String, Object> claim = Jwts.parser()
                .setSigningKey(key.getBytes())
                .parseClaimsJws(token)
                .getBody();

            // 유효한 토큰이면 true를 반환
            return true;
        } catch (JwtException e) {
            // 토큰이 유효하지 않으면 false를 반환
            return false;
        }
    }

}
