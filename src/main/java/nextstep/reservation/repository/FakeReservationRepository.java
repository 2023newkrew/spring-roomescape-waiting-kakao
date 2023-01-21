package nextstep.reservation.repository;

import nextstep.reservation.domain.Reservation;
import nextstep.theme.domain.Theme;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class FakeReservationRepository implements ReservationRepository {

    private final Map<Long, Reservation> reservations = new HashMap<>();

    private Long reservationIdIndex = 0L;


    @Override
    public boolean existsByTimetable(Reservation reservation) {
        return reservations.values()
                .stream()
                .anyMatch(r -> Objects.equals(reservation.getDate(), r.getDate()) &&
                        Objects.equals(reservation.getTime(), r.getTime()) &&
                        Objects.equals(reservation.getThemeId(), r.getThemeId()));
    }

    @Override
    public Reservation insert(Reservation reservation) {
        Theme theme = reservation.getTheme();
        if (theme
                .getId() != 1L) {
            throw new DataIntegrityViolationException("");
        }
        reservation = new Reservation(
                ++reservationIdIndex,
                reservation.getDate(),
                reservation.getTime(),
                reservation.getName(),
                theme
        );
        reservations.put(reservationIdIndex, reservation);

        return reservation;
    }

    @Override
    public Reservation getById(Long id) {
        return reservations.get(id);
    }

    @Override
    public boolean deleteById(Long id) {
        return reservations.keySet()
                .removeIf(id::equals);
    }
}
