package com.example.fastcampusmysql.application.controller;

import com.example.fastcampusmysql.application.usecase.CreatePostLikeUsecase;
import com.example.fastcampusmysql.application.usecase.CreatePostUsecase;
import com.example.fastcampusmysql.application.usecase.GetTimelinePostUsecase;
import com.example.fastcampusmysql.domain.post.dto.DailyPostCountRequestDto;
import com.example.fastcampusmysql.domain.post.dto.DailyPostCountResponseDto;
import com.example.fastcampusmysql.domain.post.dto.PostCommand;
import com.example.fastcampusmysql.domain.post.dto.PostDto;
import com.example.fastcampusmysql.domain.post.entity.Post;
import com.example.fastcampusmysql.domain.post.service.PostReadService;
import com.example.fastcampusmysql.domain.post.service.PostWriteService;
import com.example.fastcampusmysql.util.CursorRequest;
import com.example.fastcampusmysql.util.PageCursor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RequiredArgsConstructor
@RequestMapping("/posts")
@RestController
public class PostController {
    private final PostWriteService postWriteService;
    private final PostReadService postReadService;
    private final GetTimelinePostUsecase getTimelinePostUsecase;
    private final CreatePostUsecase createPostUsecase;
    private final CreatePostLikeUsecase createPostLikeUsecase;

    @PostMapping("")
    public Long create(@RequestBody PostCommand command){
        return createPostUsecase.execute(command);
    }

    @GetMapping("daily-post-counts")
    public List<DailyPostCountResponseDto> getDailyPostCounts(DailyPostCountRequestDto request){
        return postReadService.getDailyPostCount(request);
    }

    // PageRequest는 binding을 못해줌 Pageable을 이용해야함.
    @GetMapping("members/{memberId}")
    public Page<PostDto> getPosts(
            @PathVariable Long memberId,
            @PageableDefault(size=10, sort = "id", direction = Sort.Direction.DESC) Pageable pageable){
        return postReadService.getPosts(memberId, pageable);
    }

    // 오프셋 기반 페이지네이션은 중복을 볼 수 있지만, 커서 기반은 그럴 일이 없음
    @GetMapping("members/{memberId}/by-cursor")
    public PageCursor<Post> getPostsByCursor(
            @PathVariable Long memberId,
            CursorRequest cursorRequest){
        return postReadService.getPosts(memberId, cursorRequest);
    }

    @GetMapping("/members/{memberId}/timeline")
    public PageCursor<Post> getTimeline(
            @PathVariable Long memberId,
            CursorRequest cursorRequest
    ){
        return getTimelinePostUsecase.executeByTimeline(memberId, cursorRequest);
    }

    @PostMapping("/{postId}/like/v1")
    public void likePost(@PathVariable Long postId) {
//        postWriteService.likePost(postId);
        postWriteService.likePostByOptimisticLock(postId);
    }

    @PostMapping("/{postId}/like/v2")
    public void likePostV2(@PathVariable Long postId, @RequestParam Long memberId) {
//        postWriteService.likePost(postId);
        createPostLikeUsecase.execute(postId, memberId);
    }
}
