package com.example.fastcampusmysql.domain.post.repository;

import com.example.fastcampusmysql.domain.post.entity.Timeline;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface TimelineJpaRepository extends JpaRepository<Timeline, Long> {

    @Query(value = "select t from Timeline t " +
            "where t.memberId = :memberId " +
            "order by t.id desc")
    Page<Timeline> findAllByMemberIdAndOrderByIdDesc(@Param("memberId")Long memberId, Pageable pageable);

    @Query(value = "select t from Timeline t " +
            "where t.memberId = :memberId and t.id < :key " +
            "order by t.id desc")
    Page<Timeline> findAllByLessThanIdAndMemberIdAndOrderByIdDesc(@Param("key") Long key,
                                                                  @Param("memberId")Long memberId,
                                                                  Pageable pageable);
}
