package com.example.fastcampusmysql.domain.follow.service;

import com.example.fastcampusmysql.domain.follow.entity.Follow;
import com.example.fastcampusmysql.domain.follow.repository.FollowJpaRepository;
import com.example.fastcampusmysql.domain.follow.repository.FollowRepository;
import com.example.fastcampusmysql.domain.member.dto.MemberDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

@RequiredArgsConstructor
@Service
public class FollowWriteService {
    private final FollowJpaRepository followJpaRepository;
    private final FollowRepository followRepository;

    /**
     * from, to 회원 정보를 받아서 저장
     * from <-> to validate
     */
    public void create(MemberDto fromMember, MemberDto toMember){
        Assert.isTrue(!fromMember.id().equals(toMember.id()), "From, To 회원이 동일합니다.");

        Follow follow = Follow.builder()
                .fromMemberId(fromMember.id())
                .toMemberId(toMember.id())
                .build();

//        followRepository.save(follow);
        followJpaRepository.save(follow);
    }
}
