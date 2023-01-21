package nextstep.reservation.service;

import nextstep.reservation.dto.ReservationRequest;
import nextstep.reservation.dto.ReservationResponse;

public interface ReservationService {

    ReservationResponse create(ReservationRequest request);

    ReservationResponse getById(Long id);

    boolean deleteById(Long id);
}
