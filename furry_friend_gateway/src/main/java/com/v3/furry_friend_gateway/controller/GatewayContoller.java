package com.v3.furry_friend_gateway.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.v3.furry_friend_gateway.service.JwtService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/gateway")
public class GatewayContoller {

    private final JwtService jwtService;

    @GetMapping("/isvalid/{accessToken}")
    public Long isValid(@PathVariable String accessToken) {
        log.info("accessToken 확인: " + accessToken);

        // 유효성 검사
        return jwtService.validateToken(accessToken);
    }
}
