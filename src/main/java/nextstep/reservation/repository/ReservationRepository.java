package nextstep.reservation.repository;

import nextstep.reservation.domain.Reservation;

public interface ReservationRepository {

    boolean existsByScheduleId(Long scheduleId);

    boolean existsByMemberIdAndScheduleId(Long momberId, Long scheduleId);

    Reservation insert(Reservation reservation);

    Reservation getById(Long id);

    boolean deleteById(Long id);
}
