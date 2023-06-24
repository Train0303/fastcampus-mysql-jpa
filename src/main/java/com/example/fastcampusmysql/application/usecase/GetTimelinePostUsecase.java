package com.example.fastcampusmysql.application.usecase;

import com.example.fastcampusmysql.domain.follow.dto.FollowDto;
import com.example.fastcampusmysql.domain.follow.service.FollowReadService;
import com.example.fastcampusmysql.domain.post.entity.Post;
import com.example.fastcampusmysql.domain.post.entity.Timeline;
import com.example.fastcampusmysql.domain.post.service.PostReadService;
import com.example.fastcampusmysql.domain.post.service.TimelineReadService;
import com.example.fastcampusmysql.util.CursorRequest;
import com.example.fastcampusmysql.util.PageCursor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class GetTimelinePostUsecase {
    private final FollowReadService followReadService;
    private final PostReadService postReadService;
    private final TimelineReadService timelineReadService;
    /**
     * 1. memberId -> follow 조회
     * 2. 1번 결과로 게시물 조회
     */
    public PageCursor<Post> execute(Long memberId, CursorRequest cursorRequest) {
        List<FollowDto> followings = followReadService.getFollowings(memberId);
        List<Long> followingMemberIds = followings.stream()
                .map(FollowDto::toMemberId)
                .toList();

        return postReadService.getPosts(followingMemberIds, cursorRequest);
    }

    /**
     * 1. Timeline 테이블 조회
     * 2. 1번에 해당하는 게시물을 조회한다.
     */
    public PageCursor<Post> executeByTimeline(Long memberId, CursorRequest cursorRequest) {
        PageCursor<Timeline> timelines = timelineReadService.getTimelines(memberId, cursorRequest);
        List<Long> postIds = timelines.body().stream()
                .map(Timeline::getPostId)
                .toList();
        List<Post> posts = postReadService.getPosts(postIds);

        return new PageCursor<>(timelines.nextCursorRequest(), posts);
    }
}
