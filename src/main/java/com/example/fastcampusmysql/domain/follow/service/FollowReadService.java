package com.example.fastcampusmysql.domain.follow.service;

import com.example.fastcampusmysql.domain.follow.dto.FollowDto;
import com.example.fastcampusmysql.domain.follow.mapper.FollowMapper;
import com.example.fastcampusmysql.domain.follow.repository.FollowJpaRepository;
import com.example.fastcampusmysql.domain.follow.repository.FollowRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class FollowReadService {
    private final FollowMapper followMapper;
    private final FollowRepository followRepository;
    private final FollowJpaRepository followJpaRepository;

    public List<FollowDto> getFollowings(Long memberId){
        return followJpaRepository.findAllByFromMemberId(memberId).stream()
                .map(followMapper::followToFollowDto)
                .collect(Collectors.toList());

//        return followRepository.findAllByFromMemberId(memberId).stream()
//                .map(followMapper::followToFollowDto)
//                .collect(Collectors.toList());
    }

    public List<FollowDto> getFollowers(Long memberId) {
        return followJpaRepository.findALlByToMemberId(memberId).stream()
                .map(followMapper::followToFollowDto)
                .collect(Collectors.toList());

//        return followRepository.findAllByToMemberId(memberId).stream()
//                .map(followMapper::followToFollowDto)
//                .collect(Collectors.toList());
    }
}
