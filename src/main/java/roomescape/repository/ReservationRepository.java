package roomescape.repository;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.entity.Reservation;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;
import java.util.Optional;

@Repository
public class ReservationRepository {
    private static final String FIND_SQL = "select * from reservation where id = :id";
    private static final String COUNT_BY_DATETIME = "select count(*) from reservation where date = :date and time = :time";
    private static final String ADD_SQL = "insert into reservation (date, time, name, theme_name, theme_desc, theme_price) values (:date, :time, :name, :theme_name, :theme_desc, :theme_price);";
    private static final String DELETE_SQL = "delete from reservation where id = :id";
    private final NamedParameterJdbcTemplate jdbc;

    public ReservationRepository(NamedParameterJdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public Optional<Long> addReservation(Reservation reservation) {
        var foundCount = findCountReservationByDateTime(reservation.getDate(), reservation.getTime());
        if (foundCount >= 1) {
            return Optional.empty();
        }
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(ADD_SQL, new MapSqlParameterSource(Map.ofEntries(
                Map.entry("date", reservation.getDate()),
                Map.entry("time", reservation.getTime()),
                Map.entry("name", reservation.getName()),
                Map.entry("theme_name", reservation.getTheme().getName()),
                Map.entry("theme_desc", reservation.getTheme().getDesc()),
                Map.entry("theme_price", reservation.getTheme().getPrice())
        )), keyHolder);
        return Optional.ofNullable(keyHolder.getKeyAs(Long.class));
    }

    public Optional<Reservation> findReservation(long id) {
        try {
            return Optional.ofNullable(jdbc.queryForObject(FIND_SQL, Map.of("id", id), Reservation::fromRow));
        } catch (Exception e) {
            return Optional.empty();
        }

    }

    public Long findCountReservationByDateTime(LocalDate date, LocalTime time) {
        return jdbc.queryForObject(COUNT_BY_DATETIME, Map.of("date", date, "time", time), Long.class);
    }

    public int deleteReservation(long id) {
        return jdbc.update(DELETE_SQL, Map.of("id", id));
    }
}
