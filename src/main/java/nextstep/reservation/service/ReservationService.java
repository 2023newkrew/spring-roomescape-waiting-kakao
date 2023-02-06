package nextstep.reservation.service;

import nextstep.member.domain.MemberEntity;
import nextstep.reservation.domain.ReservationEntity;

public interface ReservationService {

    ReservationEntity create(ReservationEntity reservation);

    ReservationEntity getById(Long id);

    boolean deleteById(Long memberId, Long id);
}
