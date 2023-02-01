package nextstep.reservationwaiting;

import java.util.List;
import java.util.Optional;

public interface ReservationWaitingDao {
    Long save(ReservationWaiting reservationWaiting);

    List<ReservationWaiting> findByMemberId(Long memberId);

    boolean existById(Long id, Long memberId);

    Long findMaxWaitNumByScheduleId(Long scheduleId);

    void deleteById(Long id);

    Optional<ReservationWaiting> findById(Long id);
}
