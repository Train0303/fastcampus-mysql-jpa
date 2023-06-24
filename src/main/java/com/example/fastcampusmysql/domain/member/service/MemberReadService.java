package com.example.fastcampusmysql.domain.member.service;

import com.example.fastcampusmysql.domain.member.dto.MemberDto;
import com.example.fastcampusmysql.domain.member.dto.MemberNicknameHistoryDto;
import com.example.fastcampusmysql.domain.member.entity.Member;
import com.example.fastcampusmysql.domain.member.entity.MemberNicknameHistory;
import com.example.fastcampusmysql.domain.member.mapper.MemberMapper;
import com.example.fastcampusmysql.domain.member.mapper.MemberNicknameHistoryMapper;
import com.example.fastcampusmysql.domain.member.repository.MemberJpaRepository;
import com.example.fastcampusmysql.domain.member.repository.MemberNicknameHistoryJpaRepository;
import com.example.fastcampusmysql.domain.member.repository.MemberNicknameHistoryRepository;
import com.example.fastcampusmysql.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;


@RequiredArgsConstructor
@Service
public class MemberReadService {
//    private final MemberRepository memberRepository;
//    private final MemberNicknameHistoryRepository memberNicknameHistoryRepository;
    private final MemberJpaRepository memberJpaRepository;
    private final MemberNicknameHistoryJpaRepository memberNicknameHistoryJpaRepository;
    private final MemberNicknameHistoryMapper historyMapper;
    private final MemberMapper memberMapper;

    public MemberDto getMember(Long id){
//        Member member = memberRepository.findById(id).orElseThrow();
        Member member = memberJpaRepository.findById(id).orElseThrow();
        return memberMapper.memberToMemberDto(member);
    }

    public List<MemberNicknameHistoryDto> getNicknameHistories(Long memberId) {
        return memberNicknameHistoryJpaRepository.findAllByMemberId(memberId)
                .stream()
                .map(historyMapper::historyToHistoryDto)
                .collect(Collectors.toList());
    }

    public List<MemberDto> getMembers(List<Long> ids){
            return memberJpaRepository.findAllByIdIn(ids)
                    .stream()
                    .map(memberMapper::memberToMemberDto)
                    .collect(Collectors.toList());
    }
}
