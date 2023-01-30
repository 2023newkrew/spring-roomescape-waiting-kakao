package nextstep.reservation.service;

import nextstep.member.domain.MemberEntity;
import nextstep.reservation.domain.ReservationEntity;

import java.util.List;

public interface ReservationService {

    ReservationEntity create(ReservationEntity reservation);

    ReservationEntity getById(Long id);

    List<ReservationResponse> getByMemberId(Long memberId);

    boolean deleteById(Long memberId, Long id);
}
