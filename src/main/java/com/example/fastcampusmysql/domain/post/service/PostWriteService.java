package com.example.fastcampusmysql.domain.post.service;

import com.example.fastcampusmysql.domain.post.dto.PostCommand;
import com.example.fastcampusmysql.domain.post.entity.Post;
import com.example.fastcampusmysql.domain.post.repository.PostJpaCustomRepository;
import com.example.fastcampusmysql.domain.post.repository.PostJpaRepository;
import com.example.fastcampusmysql.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

// 06/23 jpa로 구현완

@RequiredArgsConstructor
@Service
@Transactional
public class PostWriteService {
    private final PostRepository postRepository;
    private final PostJpaRepository postJpaRepository;

    public Post save(Post post) {
        if(post.getId() == null) {
            return postJpaRepository.save(post);
        }
        return postJpaRepository.update(post);
    }

    public Long create(PostCommand command){
        Post post = Post.builder()
                .memberId(command.memberId())
                .contents(command.contents())
                .build();

        return save(post).getId();
    }

    @Transactional
    public void likePost(Long postId) {
        Post post = postJpaRepository.findByIdForUpdate(postId).orElseThrow();
        post.incrementLikeCount();
        save(post);
    }

    public void likePostByOptimisticLock(Long postId) {
        Post post = postJpaRepository.findById(postId).orElseThrow();
        post.incrementLikeCount();
        save(post);
    }
}
