package com.example.fastcampusmysql.domain.post.mapper;

import com.example.fastcampusmysql.domain.post.dto.DailyPostCountResponseDto;
import com.example.fastcampusmysql.domain.post.dto.PostDto;
import com.example.fastcampusmysql.domain.post.entity.Post;
import com.example.fastcampusmysql.domain.post.repository.PostLikeJpaRepository;
import com.example.fastcampusmysql.domain.post.repository.PostLikeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class PostMapper {
//    private final PostLikeRepository postLikeRepository;
    private final PostLikeJpaRepository postLikeJpaRepository;

    public PostDto postToPostDto(Post post){
        return PostDto.builder()
                .id(post.getId())
                .contents(post.getContents())
                .likeCount(postLikeJpaRepository.countById(post.getId()))
                .createdAt(post.getCreatedAt())
                .build();
    }

    public DailyPostCountResponseDto postToDailyPostCountResponseDto(Post post) {
        return DailyPostCountResponseDto.builder()
                .memberId(post.getMemberId())
                .count(postLikeJpaRepository.countById(post.getId()))
                .date(post.getCreatedDate())
                .build();
    }
}
