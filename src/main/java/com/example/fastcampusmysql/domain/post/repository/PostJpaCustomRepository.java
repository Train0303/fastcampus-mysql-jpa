package com.example.fastcampusmysql.domain.post.repository;

import com.example.fastcampusmysql.domain.post.entity.Post;

public interface PostJpaCustomRepository{
    Post update(Post post);
}
