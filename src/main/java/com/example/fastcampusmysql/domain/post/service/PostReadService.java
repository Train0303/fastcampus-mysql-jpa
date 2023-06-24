package com.example.fastcampusmysql.domain.post.service;

import com.example.fastcampusmysql.domain.post.dto.DailyPostCountRequestDto;
import com.example.fastcampusmysql.domain.post.dto.DailyPostCountResponseDto;
import com.example.fastcampusmysql.domain.post.dto.PostDto;
import com.example.fastcampusmysql.domain.post.entity.Post;
import com.example.fastcampusmysql.domain.post.mapper.PostMapper;
import com.example.fastcampusmysql.domain.post.repository.PostJpaRepository;
import com.example.fastcampusmysql.domain.post.repository.PostLikeJpaRepository;
import com.example.fastcampusmysql.domain.post.repository.PostLikeRepository;
import com.example.fastcampusmysql.domain.post.repository.PostRepository;
import com.example.fastcampusmysql.util.CursorRequest;
import com.example.fastcampusmysql.util.PageCursor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class PostReadService {
    private final PostRepository postRepository;
    private final PostJpaRepository postJpaRepository;
    private final PostLikeRepository postLikeRepository;
    private final PostMapper postMapper;

    /**
     * SELECT createdDate, memberId, count(id)
     * from Post
     * where memberId = :memberId and createdDate between firstDate and lastDate
     * group by createdDate memberId
     * @param request : 일자와 memberId가 포함
     * @return 리스트 [작성일자, 작성회원, 작성 게시물 갯수]
     */
    public List<DailyPostCountResponseDto> getDailyPostCount(DailyPostCountRequestDto request){
//        return postRepository.groupByCreatedDate(request);
        // groupBy가 적용된 jpa
        return postJpaRepository.groupByCreatedDate(request);
    }

    public Page<PostDto> getPosts(Long memberId, Pageable pageable){
        // 페이지네이션 jpa
        return postJpaRepository.findAllByMemberId(memberId, pageable).map(postMapper::postToPostDto);
    }

    //여기서부터 구현
    public PageCursor<Post> getPosts(Long memberId, CursorRequest cursorRequest){
        List<Post> posts = findAllBy(memberId, cursorRequest);
        Long nextKey = posts.stream()
                .mapToLong(Post::getId)
                .min()
                .orElse(CursorRequest.NONE_KEY);
        return new PageCursor<>(cursorRequest.next(nextKey), posts);
    }

    public PageCursor<Post> getPosts(List<Long> memberIds, CursorRequest cursorRequest){
        List<Post> posts = findAllBy(memberIds, cursorRequest);
        Long nextKey = posts.stream()
                .mapToLong(Post::getId)
                .min()
                .orElse(CursorRequest.NONE_KEY);
        return new PageCursor<>(cursorRequest.next(nextKey), posts);
    }

    public PostDto getPosts(Long postId) {
//        return postMapper.postToPostDto(postRepository.findById(postId, false).orElseThrow());
        return postMapper.postToPostDto(postJpaRepository.findById(postId).orElseThrow());

    }

    public List<Post> getPosts(List<Long> ids) {
        return postRepository.findAllByInId(ids);
    }

    private List<Post> findAllBy(Long memberId, CursorRequest cursorRequest){
        Pageable pageable = PageRequest.of(0, cursorRequest.size());
//        if (cursorRequest.hasKey()){
//            return postRepository.findAllLessThanIdAndInMemberIdAndOrderByIdDesc(cursorRequest.key(), memberId, cursorRequest.size());
//        }
//        return postRepository.findAllByMemberIdAndOrderByIdDesc(memberId, cursorRequest.size());

        if (cursorRequest.hasKey()){
            return postJpaRepository.findAllLessThanIdAndInMemberIdAndOrderByIdDesc(cursorRequest.key(), memberId, pageable)
                    .toList();
        }
        return postJpaRepository.findAllByMemberIdAndOrderByIdDesc(memberId, pageable).toList();
    }

    private List<Post> findAllBy(List<Long> memberIds, CursorRequest cursorRequest){
        if(memberIds.isEmpty()) {
            return List.of();
        }
        Pageable pageable = PageRequest.of(0, cursorRequest.size());
        if (cursorRequest.hasKey()){
            return postJpaRepository.findAllByLessThanIdAndInMemberIdAndOrderByIdDesc(cursorRequest.key(), memberIds, pageable)
                    .toList();
        }
        return postJpaRepository.findAllByInMemberIdAndOrderByIdDesc(memberIds, pageable).toList();
    }
}
