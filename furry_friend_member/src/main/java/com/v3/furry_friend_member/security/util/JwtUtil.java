package com.v3.furry_friend_member.security.util;

import java.sql.Date;
import java.time.ZonedDateTime;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.log4j.Log4j2;

@Component
@Log4j2
public class JwtUtil {

    @Value("${jwt.secretKey}")
    private String key;

    public String generateToken(int minutes, Long memberId) {

        Claims claims = Jwts.claims();
        claims.put("memberId", memberId);

        String jwtStr = Jwts.builder()
            .claim("memberId", memberId)
            .setIssuedAt(Date.from(ZonedDateTime.now().toInstant()))
            .setExpiration(Date.from(ZonedDateTime.now().plusMinutes(minutes).toInstant()))
            .signWith(SignatureAlgorithm.HS256, key)
            .compact();
        return jwtStr;
    }

    // 토큰의 유효성 검사 메서드
    public Map<String, Object> validateToken(String token)throws JwtException {

        Map<String, Object> claim = null;
        log.warn(token);
        claim = Jwts.parser()
            .setSigningKey(key) // Set Key
            .parseClaimsJws(token) // 파싱 및 검증, 실패시 에러
            .getBody();
        return claim;
    }

    // 토큰에서 ID를 출력하는 메서드
    public Long extractUserId(String token) {
        try {
            Map<String, Object> claim = validateToken(token);
            // 유효한 토큰이면 클레임을 가져와서 필요한 작업을 수행합니다.
            // 클레임은 Map<String, Object> 형식으로 반환됩니다.
            // memberId를 이용하여 필요한 작업 수행
            Integer memberIdInteger = (Integer) claim.get("memberId");

            return memberIdInteger.longValue();
            // 유효성 검사 및 클레임 활용 후의 추가 작업
        } catch (JwtException e) {
            // 토큰이 유효하지 않은 경우 예외 처리
            // 예외 처리에 맞게 적절히 대응합니다.
            log.error("JwtException:" + e);
            return null;
        }
    }

    // // 토큰을 출력하는 메서드
    // public String extractTokenFromHeader(HttpServletRequest request) {
    //     //토큰 출력
    //     return request.getHeader("Authorization");
    // }

}