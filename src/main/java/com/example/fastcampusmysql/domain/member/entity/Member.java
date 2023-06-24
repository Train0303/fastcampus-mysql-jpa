package com.example.fastcampusmysql.domain.member.entity;

import com.example.fastcampusmysql.domain.base.entity.BaseEntity;
import lombok.*;
import org.springframework.util.Assert;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@ToString(callSuper = true)
@Getter
@Table(name="Member")
public class Member extends BaseEntity {

    // hibernate로 테이블을 만든 것이 아니라면
    // 테이블 autoIncrease 설정에서 @GeneratedValue(strategy = GenerationType.IDENTITY)로 해줘야 정상적으로 동작한다.
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nickname;
    private String email;
    private LocalDate birthday;

    private final static Long NAME_MAX_LEGNTH = 10L;

    @Builder
    public Member(Long id, String nickname, String email, LocalDate birthday, LocalDateTime createdAt) {
        super(createdAt);
        this.id = id; // JPA를 고려해 Null을 넣을 수 있게 함.
        this.email = Objects.requireNonNull(email);
        this.birthday = Objects.requireNonNull(birthday);
        validateNickName(nickname);
        this.nickname = Objects.requireNonNull(nickname);
    }

    public void changeNickname(String other){
        Objects.requireNonNull(other);
        validateNickName(other);
        nickname = other;
    }

    private void validateNickName(String nickname){
        Assert.isTrue(nickname.length() <= NAME_MAX_LEGNTH, "최대 길이를 초과했습니다.");
    }

}
