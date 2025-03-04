package com.example.fastcampusmysql.domain.post.service;

import com.example.fastcampusmysql.domain.post.entity.Post;
import com.example.fastcampusmysql.domain.post.entity.Timeline;
import com.example.fastcampusmysql.domain.post.repository.TimelineJpaRepository;
import com.example.fastcampusmysql.domain.post.repository.TimelineRepository;
import com.example.fastcampusmysql.util.CursorRequest;
import com.example.fastcampusmysql.util.PageCursor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class TimelineReadService {
    private final TimelineRepository timelineRepository;
    private final TimelineJpaRepository timelineJpaRepository;

    public PageCursor<Timeline> getTimelines(Long memberId, CursorRequest cursorRequest){
        List<Timeline> timelines = findAllBy(memberId, cursorRequest);
        Long nextKey = timelines.stream()
                .mapToLong(Timeline::getId)
                .min()
                .orElse(CursorRequest.NONE_KEY);
        return new PageCursor<>(cursorRequest.next(nextKey), timelines);
    }

    private List<Timeline> findAllBy(Long memberId, CursorRequest cursorRequest){

        Pageable pageable = PageRequest.of(0, cursorRequest.size());

//        if (cursorRequest.hasKey()){
//            return timelineRepository.findAllByLessThanIdAndMemberIdAndOrderByIdDesc(cursorRequest.key(), memberId, cursorRequest.size());
//        }
//        return timelineRepository.findAllByMemberIdAndOrderByIdDesc(memberId, cursorRequest.size());

        if(cursorRequest.hasKey()) {
            return timelineJpaRepository.findAllByLessThanIdAndMemberIdAndOrderByIdDesc(cursorRequest.key(), memberId, pageable)
                    .toList();
        }
        return timelineJpaRepository.findAllByMemberIdAndOrderByIdDesc(memberId, pageable).toList();
    }
}
