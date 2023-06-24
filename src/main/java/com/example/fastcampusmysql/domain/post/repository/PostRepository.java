package com.example.fastcampusmysql.domain.post.repository;

import com.example.fastcampusmysql.util.PageHelper;
import com.example.fastcampusmysql.domain.post.dto.DailyPostCountRequestDto;
import com.example.fastcampusmysql.domain.post.dto.DailyPostCountResponseDto;
import com.example.fastcampusmysql.domain.post.entity.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class PostRepository {
    final static String TABLE = "POST";

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private static final RowMapper<Post> ROW_MAPPER = (ResultSet resultSet, int rowNum) ->
            Post.builder()
                    .id(resultSet.getLong("id"))
                    .memberId(resultSet.getLong("memberId"))
                    .contents(resultSet.getString("contents"))
                    .createdDate(resultSet.getObject("createdDate",LocalDate.class))
                    .createdAt(resultSet.getObject("createdAt", LocalDateTime.class))
                    .likeCount(resultSet.getLong("likeCount"))
                    .version(resultSet.getLong("version"))
                    .build();
    private static final RowMapper<DailyPostCountResponseDto> DAILY_POST_COUNT_MAPPER =
            (ResultSet resultSet, int rowNum) -> DailyPostCountResponseDto
                    .builder()
                    .memberId(resultSet.getLong("memberId"))
                    .date(resultSet.getObject("createdDate", LocalDate.class))
                    .count(resultSet.getLong("count"))
                    .build();

    public Post save(Post post){
        if(post.getId() == null)
            return insert(post);

        return update(post);
    }

    private Post insert(Post post){
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(namedParameterJdbcTemplate.getJdbcTemplate())
                .withTableName(TABLE)
                .usingGeneratedKeyColumns("id");

        SqlParameterSource params = new BeanPropertySqlParameterSource(post);
        Long id = jdbcInsert.executeAndReturnKey(params).longValue();

        return Post.builder()
                .id(id)
                .memberId(post.getMemberId())
                .contents(post.getContents())
                .createdDate(post.getCreatedDate())
                .createdAt(post.getCreatedAt())
                .build();
    }

    public void bulkInsert(List<Post> posts) {
        String sql = String.format("""
                INSERT INTO `%s` (memberId, contents, createdDate, createdAt)
                VALUES (:memberId, :contents, :createdDate, :createdAt)
                """, TABLE);

        SqlParameterSource[] params = posts
                .stream()
                .map(BeanPropertySqlParameterSource::new)
                .toArray(SqlParameterSource[]::new);
        namedParameterJdbcTemplate.batchUpdate(sql, params);
    }

    public Post update(Post post) {
        String sql = String.format("""
                UPDATE %s set 
                    memberId = :memberId,
                    contents = :contents,
                    createdDate = :createdDate,
                    likeCount = :likeCount,
                    createdAt = :createdAt,
                    version = :version + 1
                WHERE id = :id and version = :version
                """,TABLE);

        SqlParameterSource params = new BeanPropertySqlParameterSource(post);
        int updatedCount = namedParameterJdbcTemplate.update(sql, params);

        // 해당 로직은 버전이 맞지 않을 시 발생할 수 있다.
        // 그렇기에 단순 예외가 아닌 재처리 방안 등의 다양한 로직을 작성할 수도 있다.
        if(updatedCount == 0) {
            throw new RuntimeException("갱신실패");
        }
        return post;
    }

    public List<DailyPostCountResponseDto> groupByCreatedDate(DailyPostCountRequestDto request){
        String sql = String.format("""
                SELECT createdDate, memberId, count(id) as count
                FROM %s
                WHERE memberId = :memberId and createdDate between :firstDate and :lastDate
                GROUP BY memberId, createdDate 
                """, TABLE);

        BeanPropertySqlParameterSource params = new BeanPropertySqlParameterSource(request);
        return namedParameterJdbcTemplate.query(sql, params, DAILY_POST_COUNT_MAPPER);
    }

    /**
     * 페이지네이션을 적용한 회원수 조회
     * @param memberId : 조회하고자 하는 회원의 ID
     * @param pageRequest : 조회하고자 하는 페이지
     * @return Page<Post> : (페이지네이션을 적용한 2건의 게시글)
     */
    public Page<Post> findAllByMemberId(Long memberId, Pageable pageable){
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("memberId", memberId)
                .addValue("size", pageable.getPageSize())
                .addValue("offset", pageable.getOffset());

        String sql = String.format("""
                SELECT *
                FROM %s
                WHERE memberId = :memberId
                ORDER BY %s
                LIMIT :size
                OFFSET :offset;
                """, TABLE, PageHelper.orderBy(pageable.getSort()));

        List<Post> posts = namedParameterJdbcTemplate.query(sql, params, ROW_MAPPER);
        return new PageImpl<>(posts, pageable, getCount(memberId));
    }
    private Long getCount(Long memberId){
        String sql = String.format("""
                SELECT count(id)
                FROM %s
                WHERE memberId = :memberId
                """, TABLE);
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("memberId", memberId);
        return namedParameterJdbcTemplate.queryForObject(sql, params, Long.class);
    }

    public Optional<Post> findById(Long postId, Boolean requiredLock){
        String sql = String.format("""
                SELECT *
                FROM %s
                WHERE id = :postId
                """, TABLE);
        if (requiredLock) {
            sql += "FOR UPDATE";
        }
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("postId", postId);

        Post nullablePost = namedParameterJdbcTemplate.queryForObject(sql, params, ROW_MAPPER);
        return Optional.ofNullable(nullablePost);
    }

    public List<Post> findAllByInId(List<Long> ids){
        if(ids.isEmpty()){
            return List.of();
        }
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("ids", ids);

        String sql = String.format("""
                SELECT *
                FROM %s
                WHERE id in (:ids)
                """, TABLE);

        return namedParameterJdbcTemplate.query(sql, params, ROW_MAPPER);
    }

    public List<Post> findAllByMemberIdAndOrderByIdDesc(Long memberId, int size){
        String sql = String.format("""
                SELECT *
                FROM %s
                WHERE memberId = :memberId
                ORDER BY id desc
                LIMIT :size
                """, TABLE);
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("memberId", memberId)
                .addValue("size", size);
        return namedParameterJdbcTemplate.query(sql, params, ROW_MAPPER);
    }

    public List<Post> findAllByInMemberIdAndOrderByIdDesc(List<Long> memberIds, int size){
        if (memberIds.isEmpty()){
            return List.of();
        }

        String sql = String.format("""
                SELECT *
                FROM %s
                WHERE memberId in (:memberIds)
                ORDER BY id desc
                LIMIT :size
                """, TABLE);
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("memberIds", memberIds)
                .addValue("size", size);
        return namedParameterJdbcTemplate.query(sql, params, ROW_MAPPER);
    }

    public List<Post> findAllByLessThanIdAndMemberIdAndOrderByIdDesc(Long id, Long memberId, int size){
        String sql = String.format("""
                SELECT *
                FROM %s
                WHERE memberId = :memberId and id < :id
                ORDER BY id desc
                LIMIT :size
                """, TABLE);
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("memberId", memberId)
                .addValue("id", id)
                .addValue("size", size);

        return namedParameterJdbcTemplate.query(sql, params, ROW_MAPPER);
    }

    public List<Post> findAllByLessThanIdAndInMemberIdAndOrderByIdDesc(Long id, List<Long> memberIds, int size){
        if (memberIds.isEmpty()){
            return List.of();
        }

        String sql = String.format("""
                SELECT *
                FROM %s
                WHERE memberId in (:memberIds) and id < :id
                ORDER BY id desc
                LIMIT :size
                """, TABLE);
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("memberIds", memberIds)
                .addValue("id", id)
                .addValue("size", size);

        return namedParameterJdbcTemplate.query(sql, params, ROW_MAPPER);
    }
}