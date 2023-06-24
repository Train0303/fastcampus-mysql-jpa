package com.example.fastcampusmysql.domain.post.dto;

import lombok.Builder;

import java.time.LocalDate;

public record DailyPostCountResponseDto(
        Long memberId,
        LocalDate date,
        Long count
) {
    @Builder
    public DailyPostCountResponseDto {
    }
}
