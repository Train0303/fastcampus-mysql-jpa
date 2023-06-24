package com.example.fastcampusmysql.domain.member.dto;

import lombok.Builder;

import java.time.LocalDateTime;

public record MemberNicknameHistoryDto(
        Long id,
        Long memberId,
        String nickname,
        LocalDateTime createdAt
) {
    @Builder
    public MemberNicknameHistoryDto {
    }
}
