package com.v3.furry_friend_gateway.filter;

import java.util.List;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ServerWebExchange;

import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Mono;

@Log4j2
public class JwtAuthenticationFilter extends AbstractGatewayFilterFactory<JwtAuthenticationFilter.Config> implements GatewayFilter {

    public JwtAuthenticationFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();
            HttpHeaders headers = request.getHeaders();

            // 쿠키에서 토큰 추출
            String token = extractTokenFromCookie(headers);

            log.info("Access Token: " + token);

            if (!StringUtils.isEmpty(token)) {
                ServerHttpRequest modifiedRequest = request.mutate()
                    .header(HttpHeaders.COOKIE, "access_token=" + token)
                    .build();
                ServerWebExchange modifiedExchange = exchange.mutate().request(modifiedRequest).build();
                return chain.filter(modifiedExchange);
            } else {
                // 토큰 추출 실패 시 적절한 예외 처리 또는 로직 수행
                return Mono.error(new RuntimeException("Failed to extract token from cookie."));
            }
        };
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        GatewayFilter filter = apply(new Config());;
        return filter.filter(exchange, chain);
    }

    // 요청에서 JWT 토큰 추출하는 로직 구현
    private String extractTokenFromCookie(HttpHeaders headers) {
        List<String> cookies = headers.get(HttpHeaders.COOKIE);
        if (cookies != null) {
            for (String cookie : cookies) {
                if (cookie.startsWith("access_token=")) {
                    String[] cookieParts = cookie.split("; ");
                    for (String cookiePart : cookieParts) {
                        if (cookiePart.startsWith("access_token=")) {
                            String token = cookiePart.substring("access_token=".length());
                            return token;
                        }
                    }
                }
            }
        }
        return null;
    }



    public static class Config {
        // 필요한 구성 옵션 정의 (필요하면 사용)
    }
}
