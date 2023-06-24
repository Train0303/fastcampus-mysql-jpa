package com.example.fastcampusmysql.domain.post.dto;

import lombok.Builder;

public record PostCommand(
        Long memberId,
        String contents
) {
    @Builder
    public PostCommand {
    }
}
