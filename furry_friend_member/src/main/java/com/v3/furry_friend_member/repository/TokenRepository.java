package com.v3.furry_friend_member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.v3.furry_friend_member.entity.Token;

@Repository
public interface TokenRepository extends JpaRepository<Token, Long> {

}
