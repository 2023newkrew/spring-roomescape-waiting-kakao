package nextstep.reservation.repository;

import nextstep.reservation.domain.ReservationEntity;

public interface ReservationRepository {

    boolean existsByScheduleId(Long scheduleId);

    boolean existsByMemberIdAndScheduleId(Long momberId, Long scheduleId);

    Reservation insert(Reservation reservation);

    ReservationEntity getById(Long id);

    boolean deleteById(Long id);
}
