package com.example.fastcampusmysql.domain.member.mapper;

import com.example.fastcampusmysql.domain.member.dto.MemberDto;
import com.example.fastcampusmysql.domain.member.dto.MemberNicknameHistoryDto;
import com.example.fastcampusmysql.domain.member.entity.Member;
import com.example.fastcampusmysql.domain.member.entity.MemberNicknameHistory;
import org.springframework.stereotype.Component;

@Component
public class MemberNicknameHistoryMapper {
    /**
     * MemberNicknameHistory -> MemberNicknameHistoryDto로 변환해주는 메소드
     */
    public MemberNicknameHistoryDto historyToHistoryDto(MemberNicknameHistory history){
        return MemberNicknameHistoryDto.builder()
                .id(history.getId())
                .memberId(history.getMemberId())
                .nickname(history.getNickname())
                .createdAt(history.getCreatedAt())
                .build();
    }
}
