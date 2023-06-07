package com.v3.furry_friend_product.comment.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.v3.furry_friend_product.comment.dto.CommentDTO;
import com.v3.furry_friend_product.comment.entity.Comment;
import com.v3.furry_friend_product.comment.repository.CommentRepository;
import com.v3.furry_friend_product.product.entity.Product;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@RequiredArgsConstructor
@Log4j2
public class CommentServiceImpl implements CommentService {
    private final CommentRepository commentRepository;

    @Override
    public List<CommentDTO> getList(Long pid) {
        Product product = Product.builder().pid(pid).build();
        List<Comment> result = commentRepository.findByProduct(product);
        return result.stream().map(this::entityToDTO).collect(Collectors.toList());
    }

    @Override
    public Long register(CommentDTO commentDTO) {
        Comment comment = dtoToEntity(commentDTO);
        commentRepository.save(comment);
        return comment.getRid();
    }

    //수정과 삽입은 동일하다.
    @Override
    public Long modify(CommentDTO commentDTO) {
        Comment comment = dtoToEntity(commentDTO);
        commentRepository.save(comment);
        return comment.getRid();
    }

    @Override
    public Long remove(Long rnum) {
        commentRepository.deleteById(rnum);
        return rnum;
    }
}