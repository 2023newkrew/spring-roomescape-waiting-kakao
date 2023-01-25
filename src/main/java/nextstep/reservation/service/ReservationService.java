package nextstep.reservation.service;

import nextstep.reservation.dto.ReservationRequest;
import nextstep.reservation.dto.ReservationResponse;

public interface ReservationService {

    ReservationResponse create(Long memberId, ReservationRequest request);

    ReservationResponse getById(Long id);

    boolean deleteById(Long memberId, Long id);
}
