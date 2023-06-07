package com.v3.furry_friend_product.basket.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.v3.furry_friend_product.basket.dto.BasketRequestDTO;
import com.v3.furry_friend_product.basket.dto.BasketResponseDTO;
import com.v3.furry_friend_product.basket.service.BasketService;
import com.v3.furry_friend_product.common.service.TokenService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Controller
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/basket")
public class BasketController {

    private final BasketService basketService;

    private final TokenService tokenService;

    @GetMapping("/basket")
    public void basket(Model model, @CookieValue(name = "access_token", required = false) String accessToken){
        BasketResponseDTO basketResponseDTO = basketService.findBasketList(tokenService.getMemberId(accessToken));
        try {
            log.warn(basketResponseDTO);
            model.addAttribute("li", basketResponseDTO);
        }catch (NullPointerException e){
            log.error(e);
        }
    }

    @PostMapping("/basket")
    public String basketSave(@RequestParam(value = "pname") String pname, @CookieValue(name = "access_token", required = false) String accessToken){

        basketService.saveBasket(pname, tokenService.getMemberId(accessToken));

        return "redirect:/basket/basket";
    }

    @DeleteMapping("/basket")
    public ResponseEntity<String> basketDelete(@RequestParam(value = "bid") Long bid, @CookieValue(name = "access_token", required = false) String accessToken) {

        try {
            BasketRequestDTO basketRequestDTO = BasketRequestDTO.builder()
                .bid(bid)
                .build();
            basketService.deleteBasketItem(basketRequestDTO, tokenService.getMemberId(accessToken));

            String redirectUrl = "/basket/basket"; // 리다이렉트할 URL
            return ResponseEntity.ok().body(redirectUrl);
        } catch (Exception e) {
            // 실패했을 때의 동작을 구현
            log.error("삭제 실패");
            return ResponseEntity.badRequest().build(); // 실패 응답
        }
    }
}
