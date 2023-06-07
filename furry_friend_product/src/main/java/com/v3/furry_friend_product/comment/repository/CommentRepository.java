package com.v3.furry_friend_product.comment.repository;

import java.util.List;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.v3.furry_friend_product.comment.entity.Comment;
import com.v3.furry_friend_product.product.entity.Product;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    //테이블의 전체 데이터 가져오기 - Paging 가능
    //기본 키를 가지고 데이터 1개 가져오기
    //데이터 삽입과 수정에 사용되는 메서드 제공
    //기본 키를 가지고 삭제하는 메서드와 entity를 가지고 삭제

    //이름을 기반으로 하는 메서드 생성이 가능
    @EntityGraph(attributePaths = {"product"}, type = EntityGraph.EntityGraphType.FETCH)
    List<Comment> findByProduct(Product product);

    //회원 정보를 가지고 데이터를 삭제하는 메서드
    @Query("delete from Comment c where c.memberId = :memberId")
    void deleteByMemberId(Long memberId);

    //JPQL을 이용한 쿼리 작성 가능
    @Modifying
    @Query("update Comment r set r.memberId=null where r.memberId = :memberId")
    void updateByMember(@Param("memberId") Long memberId);
}
