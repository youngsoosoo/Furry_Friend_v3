package com.v3.furry_friend_member.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.v3.furry_friend_member.dto.MemberJoinDTO;
import com.v3.furry_friend_member.entity.Member;
import com.v3.furry_friend_member.entity.MemberRole;
import com.v3.furry_friend_member.repository.MemberRepository;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Service
public class MemberService {
    //회원이 존재하는 경우 발생시킬 예외 클래스
    static class MidExistException extends Exception{

    }

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public MemberService(MemberRepository memberRepository) {

        // 기본 생성자
        this.memberRepository = memberRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    //회원가입
    public void join(MemberJoinDTO memberJoinDTO) throws MidExistException {
        //이메일 중복 확인
        String email = memberJoinDTO.getEmail();

        boolean exist = memberRepository.existsByEmail(email);
        if(exist){
            throw new MidExistException();
        }

        //회원 가입을 위해서 입력 받은 정보를 가지고 Member Entity를 생성
        Member member = Member.builder()
            .mpw(memberJoinDTO.getMpw())
            .email(memberJoinDTO.getEmail())
            .name(memberJoinDTO.getName())
            .address(memberJoinDTO.getAddress())
            .phone(memberJoinDTO.getPhone())
            .del(memberJoinDTO.isDel())
            .social(memberJoinDTO.isSocial())
            .build();

        //비밀번호 암호화
        member.changePassword(passwordEncoder.encode(memberJoinDTO.getMpw()));
        //권한 설정
        member.addRole(MemberRole.USER);

        memberRepository.save(member);
    }
}