package nextstep.reservation.service;

import nextstep.reservation.dto.ReservationRequest;
import nextstep.reservation.dto.ReservationResponse;
import nextstep.schedule.dto.ScheduleResponse;
import nextstep.waiting.domain.Waiting;

import java.util.List;

public interface ReservationService {

    ReservationResponse create(Long memberId, ReservationRequest request, ScheduleResponse schedule);

    ReservationResponse getById(Long id);

    List<ReservationResponse> getByMemberId(Long memberId);

    boolean existsByMemberIdAndScheduleId(Long memberId, Long scheduleId);

    boolean deleteById(Long id, Long memberId, Waiting waiting);
}
