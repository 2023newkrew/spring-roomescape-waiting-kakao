package nextstep.reservation;

import static nextstep.reservation.ReservationJdbcSql.DELETE_BY_ID_STATEMENT;
import static nextstep.reservation.ReservationJdbcSql.INSERT_INTO_STATEMENT;
import static nextstep.reservation.ReservationJdbcSql.SELECT_BY_ID_AND_DATE_STATEMENT;
import static nextstep.reservation.ReservationJdbcSql.SELECT_BY_ID_STATEMENT;
import static nextstep.reservation.ReservationJdbcSql.SELECT_BY_SCHEDULE_ID_STATEMENT;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.Collections;
import java.util.List;
import nextstep.member.Member;
import nextstep.schedule.Schedule;
import nextstep.theme.Theme;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class ReservationDao {

    public final JdbcTemplate jdbcTemplate;

    public ReservationDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<Reservation> rowMapper = (resultSet, rowNum) -> Reservation.giveId(
            Reservation.builder().schedule(
                    Schedule.giveId(Schedule.builder()
                                    .theme(Theme.giveId(Theme.builder()
                                            .name(resultSet.getString("theme.name"))
                                            .desc(resultSet.getString("theme.desc"))
                                            .price(resultSet.getInt("theme.price"))
                                            .build(), resultSet.getLong("theme.id"))
                                    )
                                    .time(resultSet.getTime("schedule.time").toLocalTime())
                                    .date(resultSet.getDate("schedule.date").toLocalDate())
                                    .build()
                            , resultSet.getLong("schedule.id"))
            ).member(
                    Member.giveId(Member.builder()
                            .phone(resultSet.getString("phone"))
                            .name(resultSet.getString("name"))
                            .role(resultSet.getString("role"))
                            .username(resultSet.getString("username"))
                            .password(resultSet.getString("password")).build(), resultSet.getLong("id"))
            ).build(), resultSet.getLong("reservation.id"));


    public Long save(Reservation reservation) {
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(INSERT_INTO_STATEMENT, new String[]{"id"});
            ps.setLong(1, reservation.getSchedule().getId());
            ps.setLong(2, reservation.getMember().getId());
            return ps;

        }, keyHolder);

        return keyHolder.getKey().longValue();
    }

    public List<Reservation> findAllByThemeIdAndDate(Long themeId, String date) {

        return jdbcTemplate.query(SELECT_BY_ID_AND_DATE_STATEMENT, rowMapper, themeId, Date.valueOf(date));
    }

    public Reservation findById(Long id) {
        try {
            return jdbcTemplate.queryForObject(SELECT_BY_ID_STATEMENT, rowMapper, id);
        } catch (Exception e) {
            return null;
        }
    }

    public List<Reservation> findByScheduleId(Long id) {
        try {
            return jdbcTemplate.query(SELECT_BY_SCHEDULE_ID_STATEMENT, rowMapper, id);
        } catch (Exception e) {
            return Collections.emptyList();
        }
    }

    public void deleteById(Long id) {
        jdbcTemplate.update(DELETE_BY_ID_STATEMENT, id);
    }
}
