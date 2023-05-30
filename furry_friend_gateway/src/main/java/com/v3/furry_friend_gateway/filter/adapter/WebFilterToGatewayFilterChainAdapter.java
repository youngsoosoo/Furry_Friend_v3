package com.v3.furry_friend_gateway.filter.adapter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

public class WebFilterToGatewayFilterChainAdapter implements GatewayFilterChain {

    private final WebFilterChain webFilterChain;

    public WebFilterToGatewayFilterChainAdapter(WebFilterChain webFilterChain) {
        this.webFilterChain = webFilterChain;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange) {
        return webFilterChain.filter(exchange);
    }
}
