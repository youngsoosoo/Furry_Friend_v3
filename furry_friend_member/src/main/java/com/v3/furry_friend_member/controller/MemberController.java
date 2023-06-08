package com.v3.furry_friend_member.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.v3.furry_friend_member.dto.MemberJoinDTO;
import com.v3.furry_friend_member.service.MemberService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Controller
@Log4j2
@RequiredArgsConstructor
@RequestMapping("/member")
//member와 관련된 요청을 처리할 메서드
public class MemberController {

    @GetMapping("/login")
    //logout은 로그아웃 한 후 로그인으로 이동했을 때의 파라미터
    public void login(String logout){
        if(logout != null){
            log.info("로그아웃");
        }
    }

    private final MemberService memberService;

    //회원 가입 페이지로 이동
    @GetMapping("/join")
    public void join(){
        log.info("회원 가입 페이지로 이동");
    }

    //회원 가입 처리
    @PostMapping("/join")
    public String join(MemberJoinDTO memberJoinDTO, RedirectAttributes rattr){
        log.info(memberJoinDTO);
        try{
            memberService.join(memberJoinDTO);
            //성공
        }catch(Exception e){
            rattr.addFlashAttribute("error","mid");
            return "redirect:/member/join";
        }
        rattr.addFlashAttribute("result", "success");
        return "redirect:/member/login";
    }

    // 로그아웃 후 메인 페이지로 이동
    @GetMapping("/logout")
    public String logout(HttpServletResponse response, @CookieValue(name = "access_token", required = false) String accessToken){
        memberService.logout(response, accessToken);
        return "redirect:/member/login";
    }
}