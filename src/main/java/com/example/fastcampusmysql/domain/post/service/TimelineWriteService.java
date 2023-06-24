package com.example.fastcampusmysql.domain.post.service;

import com.example.fastcampusmysql.domain.post.entity.Timeline;
import com.example.fastcampusmysql.domain.post.repository.TimelineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class TimelineWriteService {
    private final TimelineRepository timelineRepository;
    private final EntityManager em;

    // bulkInsert의 경우에는 Jdbc를 사용하는 것이 더 좋을 것이라 판단했습니다.
    public void deliveryToTimeline(Long postId, List<Long> toMemberIds) {
        List<Timeline> timelines = toMemberIds.stream()
                .map((memberId) -> toTimeline(postId, memberId))
                .toList();

        timelineRepository.bulkInsert(timelines);
        em.flush();
        em.clear();
    }

    private static Timeline toTimeline(Long postId, Long memberId) {
        return Timeline.builder()
                .memberId(memberId)
                .postId(postId)
                .build();
    }
}
