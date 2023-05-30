package com.v3.furry_friend_gateway.controller;

import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.v3.furry_friend_gateway.service.JwtService;

import io.jsonwebtoken.JwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@Log4j2
@RequiredArgsConstructor
public class GatewayContoller {

    private final JwtService jwtService;

    @GetMapping("/isvalid")
    public boolean isValid(@CookieValue(name = "access_token", required = false) String accessToken) {
        log.info("accessToken 확인: "+accessToken);
        
        // 유효성 검사
        return jwtService.validateToken(accessToken);
    }

}
