package com.v3.furry_friend_product.common.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class TokenService {

    @Value("${token.isvalid}")
    private String url;

    public Long getMemberId(String access_token){

        // RestTemplate를 통한 API 호출
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<Long> response = null;

        try {
            // RestTemplate를 통한 API 호출
            response = restTemplate.exchange(url + access_token, HttpMethod.GET, entity, Long.class);
        } catch (RestClientException re) {
            log.error("API 호출 오류 및 재시도 실행: " + re);
            try {
                // 5초 대기 후 재시도
                Thread.sleep(5000);
                response = restTemplate.exchange(url + access_token, HttpMethod.GET, entity, Long.class);
            } catch (Exception e) {
                log.error("재시도 중 Exception 발생: " + e);
            }
        }

        log.info(response);

        // API 호출 결과에서 Long 값 추출
        Long memberId = response.getBody();

        return memberId;
    }
}
