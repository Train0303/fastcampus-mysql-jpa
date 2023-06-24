package com.example.fastcampusmysql.domain.member.service;

import com.example.fastcampusmysql.domain.member.dto.MemberDto;
import com.example.fastcampusmysql.domain.member.dto.RegisterMemberCommand;
import com.example.fastcampusmysql.domain.member.entity.Member;
import com.example.fastcampusmysql.domain.member.entity.MemberNicknameHistory;
import com.example.fastcampusmysql.domain.member.mapper.MemberMapper;
import com.example.fastcampusmysql.domain.member.repository.MemberJpaRepository;
import com.example.fastcampusmysql.domain.member.repository.MemberNicknameHistoryJpaRepository;
import com.example.fastcampusmysql.domain.member.repository.MemberNicknameHistoryRepository;
import com.example.fastcampusmysql.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional
@Service
public class MemberWriteService {
    private final MemberRepository memberRepository;
    private final MemberJpaRepository memberJpaRepository;
    private final MemberNicknameHistoryRepository memberNicknameHistoryRepository;
    private final MemberNicknameHistoryJpaRepository memberNicknameHistoryJpaRepository;
    private final MemberMapper memberMapper;
    /**
     * 목표 - 회원정보(이메일, 닉네임, 생년월일)를 등록한다.
     *     - 닉네임은 10자를 넘길 수 없다.
     * parameter : memberRegisterCommand
     * val member = Member.of(memberRegisterCommand)
     * memberRepository.save(member)
     */
    public MemberDto create(RegisterMemberCommand command) {
        Member member = Member.builder()
                .nickname(command.nickname())
                .birthday(command.birthday())
                .email(command.email())
                .build();

//        member = memberRepository.save(member);
        member = memberJpaRepository.save(member);
        System.out.println(member.getId() + " " + member.getNickname());
        saveMemberNicknameHistory(member);

        return memberMapper.memberToMemberDto(member);
    }

    // 구멍찾기
    /**
     * 회원의 이름을 변경하는 메소드
     * 1. 회원의 이름을 변경
     * 2. 변경 내역을 저장한다.
     * @param memberId
     * @param nickname
     */
    public void changeNickname(Long memberId, String nickname){
        Member member = memberRepository.findById(memberId).orElseThrow();
        member.changeNickname(nickname);
//        memberRepository.save(member);
        memberJpaRepository.save(member);
        // TODO: 변경내역 히스토리를 저장한다.
        saveMemberNicknameHistory(member);
    }

    private void saveMemberNicknameHistory(Member member){
        MemberNicknameHistory history = MemberNicknameHistory.builder()
                .memberId(member.getId())
                .nickname(member.getNickname())
                .build();
//        throw new RuntimeException("강제로 예외 발생시키기");
//        memberNicknameHistoryRepository.save(history);
        memberNicknameHistoryJpaRepository.save(history);
    }
}
