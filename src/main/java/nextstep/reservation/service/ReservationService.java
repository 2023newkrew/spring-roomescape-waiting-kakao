package nextstep.reservation.service;

import nextstep.reservation.dto.ReservationRequest;
import nextstep.reservation.dto.ReservationResponse;

import java.util.List;

public interface ReservationService {

    ReservationResponse create(Long memberId, ReservationRequest request);

    ReservationResponse getById(Long id);

    List<ReservationResponse> getByMemberId(Long memberId);

    boolean deleteById(Long memberId, Long id);
}
