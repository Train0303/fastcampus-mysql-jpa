package com.example.fastcampusmysql.domain.member.repository;

import com.example.fastcampusmysql.domain.member.entity.MemberNicknameHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MemberNicknameHistoryJpaRepository extends JpaRepository<MemberNicknameHistory, Long> {
    List<MemberNicknameHistory> findAllByMemberId(Long memberId);
}
