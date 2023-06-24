package com.example.fastcampusmysql.domain.post.dto;

import lombok.Builder;

import java.time.LocalDateTime;

public record PostDto(
        Long id,
        String contents,
        LocalDateTime createdAt,
        Long likeCount
) {
    @Builder
    public PostDto {
    }
}
