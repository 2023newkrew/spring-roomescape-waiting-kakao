package roomescape.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.entity.Theme;
import roomescape.entity.Waiting;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class WaitingRepository {
    private static final String SELECT_BY_ID = """
            select w.id, w.date, w.time, w.name, w.member_id, w.wait_number, t.id, t.name, t.desc, t.price
            from (
                select *, row_number() over (partition by theme_id, date, time order by id) as wait_number from waiting
            ) w
            inner join theme t on t.id = w.theme_id
            where w.id = :id
            """;
    private static final String SELECT_BY_MEMBER_ID = """
            select w.id, w.date, w.time, w.name, w.member_id, w.wait_number, t.id, t.name, t.desc, t.price
            from (
                select *, row_number() over (partition by theme_id, date, time order by id) as wait_number from waiting
            ) w
            inner join theme t on t.id = w.theme_id
            where w.member_id = :member_id
            """;
    private static final String SELECT_FIRST_BY_GROUP = """
            select w.id, w.date, w.time, w.name, w.member_id, w.wait_number, t.id, t.name, t.desc, t.price
            from (
                select *, row_number() over (partition by theme_id, date, time order by id) as wait_number from waiting
            ) w
            inner join theme t on t.id = w.theme_id
            where
                w.theme_id = :theme_id
                and w.date = :date
                and w.time = :time
            order by w.wait_number
            limit 1
            """;
    private static final String INSERT = """
            insert into waiting         (date   , time  , name  , theme_id  , member_id     )
            values                      (:date  , :time , :name , :theme_id , :member_id    )
            """;
    private static final String DELETE = "delete from waiting where id = :id";
    private static final RowMapper<Waiting> ROW_MAPPER = (rs, rowNum) -> new Waiting(
            rs.getLong(1),
            rs.getDate(2).toLocalDate(),
            rs.getTime(3).toLocalTime(),
            rs.getString(4),
            rs.getLong(5),
            rs.getInt(6),
            new Theme(
                    rs.getLong(7),
                    rs.getString(8),
                    rs.getString(9),
                    rs.getInt(10)
            )
    );
    private final NamedParameterJdbcTemplate jdbc;


    public Long insert(String name, LocalDate date, LocalTime time, Long themeId, Long memberId) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(INSERT, new MapSqlParameterSource(Map.ofEntries(
                Map.entry("name", name),
                Map.entry("date", date),
                Map.entry("time", time),
                Map.entry("theme_id", themeId),
                Map.entry("member_id", memberId)
        )), keyHolder);
        return keyHolder.getKeyAs(Long.class);
    }

    public Optional<Waiting> selectById(long id) {
        return jdbc.queryForStream(SELECT_BY_ID, Map.of("id", id), ROW_MAPPER).findFirst();
    }

    public List<Waiting> selectByMemberId(long id) {
        return jdbc.queryForStream(SELECT_BY_MEMBER_ID, Map.of("member_id", id), ROW_MAPPER)
                   .collect(Collectors.toList());
    }

    public Optional<Waiting> selectFirstByGroup(long themeId, LocalDate date, LocalTime time) {
        return jdbc.queryForStream(
                           SELECT_FIRST_BY_GROUP,
                           Map.of(
                                   "theme_id", themeId,
                                   "date", date,
                                   "time", time
                           ),
                           ROW_MAPPER
                   )
                   .findFirst();
    }

    public int delete(long id) {
        return jdbc.update(DELETE, Map.of("id", id));
    }
}
