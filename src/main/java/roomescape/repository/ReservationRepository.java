package roomescape.repository;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.entity.Reservation;
import roomescape.entity.Theme;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;
import java.util.Optional;

@Repository
public class ReservationRepository {
    private static final String SELECT_BY_ID = "select r.id, r.date, r.time, r.name, r.theme_id, t.name, t.desc, t.price from reservation r inner join theme t on t.id = r.theme_id where r.id = :id";
    private static final String INSERT = "insert into reservation (date, time, name, theme_id) values (:date, :time, :name, :theme_id);";
    private static final String DELETE = "delete from reservation where id = :id";
    private static final RowMapper<Reservation> ROW_MAPPER = (rs, rowNum) -> new Reservation(
            rs.getLong(1),
            rs.getDate(2).toLocalDate(),
            rs.getTime(3).toLocalTime(),
            rs.getString(4),
            new Theme(
                    rs.getLong(5),
                    rs.getString(6),
                    rs.getString(7),
                    rs.getInt(8)
            )

    );
    private final NamedParameterJdbcTemplate jdbc;

    public ReservationRepository(NamedParameterJdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    /**
     * @return unique 제약조건에 의해서 특정 상황에서 삽입 불가능할 수 있음 따라서 {@link Optional}타입
     */
    public Optional<Long> insert(String name, LocalDate date, LocalTime time, Long themeId) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        try {
            jdbc.update(INSERT, new MapSqlParameterSource(Map.ofEntries(
                    Map.entry("name", name),
                    Map.entry("date", date),
                    Map.entry("time", time),
                    Map.entry("theme_id", themeId)
            )), keyHolder);
        } catch (DataAccessException dataAccessException) {
            return Optional.empty();
        }
        return Optional.ofNullable(keyHolder.getKeyAs(Long.class));
    }

    public Optional<Reservation> selectById(long id) {
        return jdbc.queryForStream(SELECT_BY_ID, Map.of("id", id), ROW_MAPPER).findFirst();
    }

    public int delete(long id) {
        return jdbc.update(DELETE, Map.of("id", id));
    }
}
