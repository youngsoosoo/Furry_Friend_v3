package com.v3.furry_friend_product.comment.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.v3.furry_friend_product.comment.dto.CommentDTO;
import com.v3.furry_friend_product.comment.service.CommentService;
import com.v3.furry_friend_product.common.service.TokenService;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/reviews")
public class CommentController {
    private final CommentService commentService;

    private final TokenService tokenService;

    //상품 번호에 해당하는 댓글 목록을 처리
    @GetMapping("/{pid}/list")
    public ResponseEntity<List<CommentDTO>> list(@PathVariable("pid") Long pid){
        List<CommentDTO> result = commentService.getList(pid);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    //댓글 추가
    @PostMapping("/{pid}")
    public ResponseEntity<Long> addReview(@PathVariable("pid") Long pid, @RequestBody CommentDTO commentDTO, @CookieValue(name = "access_token", required = false) String accessToken) {
        commentDTO.setMid(tokenService.getMemberId(accessToken));
        //@RequestBody는 json데이터를 받아서 ReviewDTO로 받고 이름이 일치하는 것끼리 매칭을 한다.
        Long result = commentService.register(commentDTO);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    //댓글 수정
    @PutMapping("/{rid}")
    public ResponseEntity<Long> updateReview(@PathVariable("rid") Long rid,
                                          @RequestBody CommentDTO commentDTO, @CookieValue(name = "access_token", required = false) String accessToken){
        commentDTO.setMid(tokenService.getMemberId(accessToken));
        Long result = commentService.modify(commentDTO);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    //댓글 삭제
    @DeleteMapping("/{rid}")
    public ResponseEntity<Long> deleteReview(@PathVariable("rid") Long rid){
        Long result = commentService.remove(rid);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
