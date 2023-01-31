package nextstep.reservation.service;

import nextstep.reservation.domain.Reservation;
import nextstep.reservation.domain.ReservationEntity;

public interface ReservationService {

    ReservationEntity create(Reservation reservation);

    ReservationEntity getById(Long id);

    boolean deleteById(Long memberId, Long id);
}
