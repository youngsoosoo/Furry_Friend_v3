package com.v3.furry_friend_gateway.config;

import java.util.Arrays;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import com.v3.furry_friend_gateway.filter.JwtAuthenticationFilter;

@Configuration
public class GatewaySecurityConfig {
    // Gateway에 대한 보안 구성을 담당하는 설정 클래스입니다.

    // JWT 인증 필터를 빈으로 등록합니다.
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter();
    }

    // SecurityWebFilterChain을 설정하여 Gateway의 보안 설정을 정의합니다.
    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
            .cors(httpSecurityCorsConfigurer -> {
                httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource());
            })
            .csrf().disable()
            .logout().disable()
            .authorizeExchange()
            .pathMatchers("/**").permitAll()
            .anyExchange().authenticated()
            .and()
            .build();
    }


    // CORS(Cross-Origin Resource Sharing) 설정을 위한 CorsConfigurationSource를 생성합니다.
    @Bean
    public CorsConfigurationSource corsConfigurationSource(){

        CorsConfiguration configuration = new CorsConfiguration();
        // 모든 요청에 대해 허용된 Origin 패턴을 설정합니다.
        configuration.setAllowedOriginPatterns(Arrays.asList("*"));
        // 허용된 HTTP 메서드를 설정합니다.
        configuration.setAllowedMethods(Arrays.asList(
            "HEAD", "GET", "POST", "PUT", "DELETE", "PATCH"));
        // 허용된 헤더를 설정합니다.
        configuration.setAllowedHeaders(Arrays.asList(
            "Authorization", "Cache-Control", "Content-Type"));
        // 인증 설정을 활성화합니다.
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    // RouteLocator를 사용하여 경로별로 라우팅 설정을 정의합니다.
    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
            // Member 서비스로의 요청을 member-service로 라우팅합니다.
            .route("member-service", r -> r.path("/member/**")
                .filters(f -> f.rewritePath("/member/(?<path>.*)", "/member/${path}"))
                .uri("http://localhost:8081"))
            // OAuth2 로그인 관련 요청을 oauth2-login-service로 라우팅합니다.
            .route("oauth2-login-service", r -> r
                .path("/oauth2/**")
                .filters(f -> f.rewritePath("/oauth2/(?<path>.*)", "/oauth2/${path}"))
                .uri("http://localhost:8081"))
            // 카카오 로그인을 위한 라우팅 설정
            .route("kakao-login", r -> r
                .path("/login/oauth2/code/kakao/**") // 수정된 경로 설정
                .filters(f -> f.rewritePath("/login/oauth2/code/kakao/(?<path>.*)", "/oauth2/code/kakao/${path}")) // 수정된 리라이팅 설정
                .uri("http://localhost:8081"))
            // product-service로의 요청을 라우팅합니다.
            .route("product-service", r -> r.path("/product/**")
                .filters(f -> f.filter(jwtAuthenticationFilter()).rewritePath("/product/(?<path>.*)", "/product/${path}"))
                .uri("lb://localhost:8080/"))
            // reviews-service로의 요청을 라우팅합니다.
            .route("reviews-service", r -> r.path("/reviews/**")
                .filters(f -> f.filter(jwtAuthenticationFilter()).rewritePath("/reviews/(?<path>.*)", "/reviews/${path}"))
                .uri("lb://localhost:8080/"))
            // basket-service로의 요청을 라우팅합니다.
            .route("basket-service", r -> r.path("/basket/**")
                .filters(f -> f.filter(jwtAuthenticationFilter()).rewritePath("/basket/(?<path>.*)", "/basket/${path}"))
                .uri("lb://localhost:8080/"))
            .build();
    }
}
