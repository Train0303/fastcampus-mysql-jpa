package com.example.fastcampusmysql.domain.follow.mapper;

import com.example.fastcampusmysql.domain.follow.dto.FollowDto;
import com.example.fastcampusmysql.domain.follow.entity.Follow;
import org.springframework.stereotype.Component;

@Component
public class FollowMapper {
    /*
        Follow -> FollowDto 변환 Mapper
     */
    public FollowDto followToFollowDto(Follow follow){
        return FollowDto.builder()
                .id(follow.getId())
                .fromMemberId(follow.getFromMemberId())
                .toMemberId(follow.getToMemberId())
                .build();
    }
}
