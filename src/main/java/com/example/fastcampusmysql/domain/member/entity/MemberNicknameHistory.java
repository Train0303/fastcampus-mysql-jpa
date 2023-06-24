package com.example.fastcampusmysql.domain.member.entity;

import com.example.fastcampusmysql.domain.base.entity.BaseEntity;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;


// 히스토리성 데이터는 정규화의 대상이 아니다.
// 최신성 vs 과거의 기록을 남겨야 하는지를 계속 생각
// 이는 기획자와 담당자와의 커뮤니케이션을 통해 해결해야 한다.
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(callSuper = true)
@Entity
@Table(name="MemberNicknameHistory")
public class MemberNicknameHistory extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long memberId;

    private String nickname;

    @Builder
    public MemberNicknameHistory(Long id, Long memberId, String nickname, LocalDateTime createdAt) {
        super(createdAt);
        this.id = id;
        this.memberId = Objects.requireNonNull(memberId);
        this.nickname = Objects.requireNonNull(nickname);
    }
}
