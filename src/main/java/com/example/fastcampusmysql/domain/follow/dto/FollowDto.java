package com.example.fastcampusmysql.domain.follow.dto;

import lombok.Builder;

public record FollowDto(
        Long id,
        Long fromMemberId,
        Long toMemberId
) {
    @Builder
    public FollowDto {
    }
}
