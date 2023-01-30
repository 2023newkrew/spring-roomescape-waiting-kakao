package nextstep.reservation.repository;

import nextstep.reservation.domain.ReservationEntity;

import java.util.List;

public interface ReservationRepository {

    boolean existsByScheduleId(Long scheduleId);

    boolean existsByMemberIdAndScheduleId(Long momberId, Long scheduleId);

    Reservation insert(Reservation reservation);

    Reservation getById(Long id);

    List<Reservation> getByMemberId(Long memberId);

    boolean updateById(Long id, Long memberId);

    boolean deleteById(Long id);
}
