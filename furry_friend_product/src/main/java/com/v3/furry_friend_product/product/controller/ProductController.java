package com.v3.furry_friend_product.product.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.v3.furry_friend_product.common.service.TokenService;
import com.v3.furry_friend_product.product.dto.PageRequestDTO;
import com.v3.furry_friend_product.product.dto.PageResponseDTO;
import com.v3.furry_friend_product.product.dto.ProductDTO;
import com.v3.furry_friend_product.product.service.ProductService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Controller
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {
    private final ProductService productService;

    private final TokenService tokenService;

    @GetMapping("/register")
    public void register(){

    }

    @PostMapping("/register")
    public String register(ProductDTO productDTO, RedirectAttributes redirectAttributes, @CookieValue(name = "access_token", required = false) String accessToken){
        log.info("productDTO:" + productDTO);
        productDTO.setMid(tokenService.getMemberId(accessToken));
        Long pid = productService.register(productDTO);
        redirectAttributes.addFlashAttribute("msg", pid + "삽입 성공");
        return "redirect:/product/list";
    }

    @GetMapping("/list")
    public void list(PageRequestDTO pageRequestDTO, Model model){

        PageResponseDTO pageResponseDTO = productService.getList(pageRequestDTO);
        model.addAttribute("result", pageResponseDTO);

    }

    @GetMapping("/read")
    public void read(Long pid, @ModelAttribute("requestDTO") PageRequestDTO pageRequestDTO, Model model, @CookieValue(name = "access_token", required = false) String accessToken){
        ProductDTO productDTO = productService.getProduct(pid);
        model.addAttribute("dto", productDTO);
        model.addAttribute("mid", tokenService.getMemberId(accessToken));
    }
}
