package com.example.fastcampusmysql.domain.member.repository;

import com.example.fastcampusmysql.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class MemberRepository {
    private final EntityManager em;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final static String TABLE = "member";
    private final static String SELECT_FOLLOW_WHERE_ID = String.format("SELECT * FROM %s WHERE id ", TABLE);

    private final static RowMapper<Member> rowMapper = (ResultSet resultSet, int rowNum) -> Member
            .builder()
            .id(resultSet.getLong("id"))
            .email(resultSet.getString("email"))
            .nickname(resultSet.getString("nickname"))
            .birthday(resultSet.getObject("birthday", LocalDate.class))
            .createdAt(resultSet.getObject("createdAt", LocalDateTime.class))
            .build();
    /**
     * id로 멤버 데이터를 조회하는 메소드
     * select * from Member where id =: id
     * @param id: Member의 ID
     * @return id를 통해얻은 Member 데이터
     */
    public Optional<Member> findById(Long id){
        String sql = SELECT_FOLLOW_WHERE_ID + "= :id";
        MapSqlParameterSource param = new MapSqlParameterSource()
                .addValue("id", id);

        // beanPropertyRowMapper를 사용하면 일일이 맵핑을 안해도 된다.
        // 하지만 엔티티나 dto에 모든 필드에 대해 setter를 열어야 하므로 좋은 방법은 아니다.

//        List<Member> member = namedParameterJdbcTemplate.query(sql, param, rowMapper);
        Member member = namedParameterJdbcTemplate.queryForObject(sql, param, rowMapper);
        return Optional.ofNullable(member);
    }

    public List<Member> findAllByIdIn(List<Long> ids){
        // ids가 빈 리스트라면 sql을 진행할 이유가 없으니 빈 리스트 반환;
        if(ids.isEmpty())
            return List.of();

        String sql = SELECT_FOLLOW_WHERE_ID + "in (:ids)";
        MapSqlParameterSource params = new MapSqlParameterSource()
                .addValue("ids", ids);
        return namedParameterJdbcTemplate.query(sql, params, rowMapper);
    }

    /**
     * member id를 보고 갱신 또는 삽입을 정함
     * 반환값은 id를 담아서 반환한다.
     * @param member : 입력하고자 하는 멤버 클래스
     * @return Member타입
     */
    public Member save(Member member){
        if(member.getId() == null){
            return insert(member);
        }

        return update(member);
    }

    private Member insert(Member member){
        SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert(namedParameterJdbcTemplate.getJdbcTemplate())
                .withTableName("Member")
                .usingGeneratedKeyColumns("id");

        SqlParameterSource params = new BeanPropertySqlParameterSource(member);
        Long id = simpleJdbcInsert.executeAndReturnKey(params).longValue();
        return Member.builder()
                .id(id)
                .email(member.getEmail())
                .nickname(member.getNickname())
                .birthday(member.getBirthday())
                .createdAt(member.getCreatedAt())
                .build();

    }

    private Member update(Member member){
        // TODO: need implements
        String sql = String.format("UPDATE %s set email = :email, nickname = :nickname, birthday = :birthday WHERE id= :id", TABLE);
        SqlParameterSource parameterSource = new BeanPropertySqlParameterSource(member);
        namedParameterJdbcTemplate.update(sql, parameterSource);
        return member;
    }
}
