package com.v3.furry_friend_member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class MemberJoinDTO {
    private Long mid;
    private String mpw;
    private String email;
    private String name;
    private String address;
    private String phone;
    private boolean del;
    private boolean social;
}
