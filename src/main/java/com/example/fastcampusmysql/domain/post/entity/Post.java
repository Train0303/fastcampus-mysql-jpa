package com.example.fastcampusmysql.domain.post.entity;

import com.example.fastcampusmysql.domain.base.entity.BaseEntity;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true)
@Entity
public class Post extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long memberId;
    private String contents;
    private Long likeCount;
    private Long version;
    private LocalDate createdDate;

    @Builder
    public Post(LocalDateTime createdAt, Long id, Long memberId, String contents, Long likeCount, Long version, LocalDate createdDate) {
        super(createdAt);
        this.id = id;
        this.memberId = Objects.requireNonNull(memberId);
        this.contents = Objects.requireNonNull(contents);
        this.likeCount = likeCount == null ? 0 : likeCount;
        this.version = version == null ? 0 : version;
        this.createdDate = createdDate == null ? LocalDate.now() : createdDate;
    }

    public void incrementLikeCount() {
        likeCount++;
    }
}
