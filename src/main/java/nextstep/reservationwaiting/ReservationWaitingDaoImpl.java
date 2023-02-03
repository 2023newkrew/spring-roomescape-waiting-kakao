package nextstep.reservationwaiting;

import static nextstep.reservationwaiting.ReservationWaitingJdbcSql.COUNT_BY_WAIT_NUM_STATEMENT;
import static nextstep.reservationwaiting.ReservationWaitingJdbcSql.DELETE_BY_ID_STATEMENT;
import static nextstep.reservationwaiting.ReservationWaitingJdbcSql.EXIST_BY_ID_STATEMENT;
import static nextstep.reservationwaiting.ReservationWaitingJdbcSql.INSERT_INTO_STATEMENT;
import static nextstep.reservationwaiting.ReservationWaitingJdbcSql.SELECT_BY_ID_STATEMENT;
import static nextstep.reservationwaiting.ReservationWaitingJdbcSql.SELECT_BY_MEMBER_ID_STATEMENT;
import static nextstep.reservationwaiting.ReservationWaitingJdbcSql.SELECT_MAX_WAIT_NUM_BY_SCHEDULE_ID_STATEMENT;
import static nextstep.reservationwaiting.ReservationWaitingJdbcSql.STATUS_TO_DROPPED_STATEMENT;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import nextstep.schedule.Schedule;
import nextstep.theme.Theme;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

@Repository
public class ReservationWaitingDaoImpl implements ReservationWaitingDao {

    public final JdbcTemplate jdbcTemplate;

    public ReservationWaitingDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final RowMapper<ReservationWaiting> rowMapper = (resultSet, rowNum) -> ReservationWaiting.giveId(
            ReservationWaiting.builder().schedule(Schedule.giveId(Schedule.builder().theme(Theme.giveId(
                                    Theme.builder().name(resultSet.getString("theme.name")).desc(resultSet.getString("theme.desc"))
                                            .price(resultSet.getInt("theme.price")).build(), resultSet.getLong("theme.id")))
                            .time(resultSet.getTime("schedule.time").toLocalTime())
                            .date(resultSet.getDate("schedule.date").toLocalDate()).build(), resultSet.getLong("schedule.id")))
                    .waitNum(resultSet.getLong("reservation_waiting.wait_num"))
                    .memberId(resultSet.getLong("reservation_waiting.member_id"))
                    .status(ReservationWaitingStatus.of(resultSet.getInt("reservation_waiting.wait_status"))).build(),
            resultSet.getLong("reservation_waiting.id"));

    @Override
    public Long save(ReservationWaiting reservationWaiting) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        reservationWaiting.toFinished();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(INSERT_INTO_STATEMENT, new String[]{"id"});
            ps.setLong(1, reservationWaiting.getSchedule().getId());
            ps.setLong(2, reservationWaiting.getMemberId());
            ps.setLong(3, reservationWaiting.getWaitNum());
            ps.setInt(4, reservationWaiting.getStatus().value());
            return ps;

        }, keyHolder);
        return keyHolder.getKeyAs(Long.class);
    }

    @Override
    public List<ReservationWaiting> findByMemberId(Long memberId) {
        try {
            return jdbcTemplate.query(SELECT_BY_MEMBER_ID_STATEMENT, rowMapper, memberId);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public boolean existById(Long id, Long memberId) {
        try {
            return Objects.requireNonNull(
                    jdbcTemplate.queryForObject(EXIST_BY_ID_STATEMENT, Integer.class, memberId, id)) == 1;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Long findMaxWaitNumByScheduleId(Long scheduleId) {
        try {
            return Objects.requireNonNull(
                    jdbcTemplate.queryForObject(SELECT_MAX_WAIT_NUM_BY_SCHEDULE_ID_STATEMENT, Long.class, scheduleId));
        } catch (Exception e) {
            return 0L;
        }
    }

    @Override
    public void deleteById(Long id) {
        jdbcTemplate.update(DELETE_BY_ID_STATEMENT, id);
    }

    @Override
    public Optional<ReservationWaiting> findById(Long id, Long memberId) {
        return jdbcTemplate.query(SELECT_BY_ID_STATEMENT, rowMapper, id, memberId).stream().findAny();
    }

    @Override
    public int countFinishedByScheduleIdAndWaitNum(Long scheduleId, Long waitNum) {
        return jdbcTemplate.query(COUNT_BY_WAIT_NUM_STATEMENT, (rs, rowNum) -> rs.getInt(1), scheduleId, waitNum)
                .stream().findFirst().orElse(0);
    }

    @Override
    public void dropById(Long memberId, Long id) {
        jdbcTemplate.update(STATUS_TO_DROPPED_STATEMENT, memberId, id);
    }

}

