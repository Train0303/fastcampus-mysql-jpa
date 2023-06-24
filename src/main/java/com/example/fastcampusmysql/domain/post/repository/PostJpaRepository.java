package com.example.fastcampusmysql.domain.post.repository;

import com.example.fastcampusmysql.domain.post.dto.DailyPostCountRequestDto;
import com.example.fastcampusmysql.domain.post.dto.DailyPostCountResponseDto;
import com.example.fastcampusmysql.domain.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.persistence.LockModeType;
import java.util.List;
import java.util.Optional;

public interface PostJpaRepository extends JpaRepository<Post, Long>, PostJpaCustomRepository {

    Long countById(Long id);

    @Query(value = "select p from Post p where p.memberId = :memberId")
    Page<Post> findAllByMemberId(@Param("memberId") Long memberId, Pageable pageable);

    @Query(value = "select new com.example.fastcampusmysql.domain.post.dto.DailyPostCountResponseDto(" +
            "p.memberId, p.createdDate, count(p)) from Post p " +
            "where p.memberId = :#{#dPCR.memberId()} and p.createdDate between :#{#dPCR.firstDate()} and :#{#dPCR.lastDate()} " +
            "group by p.memberId, p.createdDate")
    List<DailyPostCountResponseDto> groupByCreatedDate(@Param("dPCR") DailyPostCountRequestDto dailyPostCountRequestDto);

    @Lock(value = LockModeType.PESSIMISTIC_WRITE)
    @Query(value = "select p from Post p where p.id = :id")
    Optional<Post> findByIdForUpdate(@Param("id") Long id);


    // jpql방식으로는 limit쿼리를 넣을 수 없다.
    // 그래서 Pageable을 넣어주는 방식을 사용한다.
    @Query(value = "select p from Post p " +
            "where p.memberId in (:memberIds) " +
            "order by p.id desc ")
    Page<Post> findAllByInMemberIdAndOrderByIdDesc(@Param("memberIds") List<Long> memberIds, Pageable pageable);


    @Query(value = "select p from Post p " +
            "where p.memberId in (:memberIds) and p.id < :key " +
            "order by p.id desc")
    Page<Post> findAllByLessThanIdAndInMemberIdAndOrderByIdDesc(@Param("key")Long key,
                                                                @Param("memberIds") List<Long> memberIds,
                                                                Pageable pageable);

    @Query(value = "select p from Post p " +
            "where p.memberId = :memberId " +
            "order by p.id desc ")
    Page<Post> findAllByMemberIdAndOrderByIdDesc(@Param("memberId") Long memberId, Pageable pageable);


    @Query(value = "select p from Post p " +
            "where p.memberId = :memberId and p.id < :key " +
            "order by p.id desc")
    Page<Post> findAllLessThanIdAndInMemberIdAndOrderByIdDesc(@Param("key")Long key,
                                                                @Param("memberId") Long memberId,
                                                                Pageable pageable);
}
