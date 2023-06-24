package com.example.fastcampusmysql.domain.member.mapper;

import com.example.fastcampusmysql.domain.member.dto.MemberDto;
import com.example.fastcampusmysql.domain.member.entity.Member;
import org.springframework.stereotype.Component;

@Component
public class MemberMapper {
    /**
     * Member -> MemberDto로 변환해주는 메소드
     */
    public MemberDto memberToMemberDto(Member member){
        return MemberDto.builder()
                .id(member.getId())
                .email(member.getEmail())
                .nickname(member.getNickname())
                .birthday(member.getBirthday())
                .build();
    }
}
