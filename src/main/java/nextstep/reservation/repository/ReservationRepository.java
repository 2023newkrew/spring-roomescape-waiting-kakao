package nextstep.reservation.repository;

import nextstep.reservation.domain.ReservationEntity;

public interface ReservationRepository {

    boolean existsByScheduleId(Long scheduleId);

    ReservationEntity insert(ReservationEntity reservation);

    ReservationEntity getById(Long id);

    boolean deleteById(Long id);
}
