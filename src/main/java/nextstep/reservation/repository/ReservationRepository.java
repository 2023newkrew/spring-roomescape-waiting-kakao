package nextstep.reservation.repository;

import nextstep.reservation.domain.Reservation;

public interface ReservationRepository {

    boolean existsByTimetable(Reservation reservation);

    Reservation insert(Reservation reservation);

    Reservation getById(Long id);

    boolean deleteById(Long id);
}
