package com.example.fastcampusmysql.domain.post.service;

import com.example.fastcampusmysql.domain.member.dto.MemberDto;
import com.example.fastcampusmysql.domain.post.dto.PostDto;
import com.example.fastcampusmysql.domain.post.entity.Post;
import com.example.fastcampusmysql.domain.post.entity.PostLike;
import com.example.fastcampusmysql.domain.post.repository.PostLikeJpaRepository;
import com.example.fastcampusmysql.domain.post.repository.PostLikeRepository;
import com.example.fastcampusmysql.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class PostLikeWriteService {
    private final PostLikeJpaRepository postLikeJpaRepository;
//    private final PostLikeRepository postLikeRepository;

    public Long create(PostDto postDto, MemberDto memberDto) {
        PostLike postLike = PostLike
                .builder()
                .postId(postDto.id())
                .memberId(memberDto.id())
                .build();

        return postLikeJpaRepository.save(postLike).getPostId();
    }
}
