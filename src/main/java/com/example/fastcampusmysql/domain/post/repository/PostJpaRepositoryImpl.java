package com.example.fastcampusmysql.domain.post.repository;

import com.example.fastcampusmysql.domain.post.entity.Post;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
public class PostJpaRepositoryImpl implements PostJpaCustomRepository{

    @Autowired
    EntityManager em;

    public Post update(Post post) {
        int updatedCount = em.createQuery("update Post p set " +
                "p.memberId = :memberId, " +
                "p.contents = :contents, " +
                "p.likeCount = :likeCount, " +
                "p.createdAt = :createdAt, " +
                "p.version = :version + 1L " +
                "where id = :id and version = :version")
                .setParameter("id", post.getId())
                .setParameter("memberId", post.getMemberId())
                .setParameter("contents", post.getContents())
                .setParameter("likeCount", post.getLikeCount())
                .setParameter("createdAt", post.getCreatedAt())
                .setParameter("version", post.getVersion())
                .executeUpdate();

        if(updatedCount == 0) {
            throw new RuntimeException("갱신실패");
        }
        return post;
    }

}
