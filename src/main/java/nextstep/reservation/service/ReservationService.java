package nextstep.reservation.service;

import nextstep.member.domain.MemberEntity;
import nextstep.reservation.domain.ReservationEntity;

import java.util.List;

public interface ReservationService {

    ReservationEntity create(ReservationEntity reservation);

    ReservationEntity getById(Long id);

    List<ReservationEntity> getByMember(MemberEntity member);

    boolean deleteById(MemberEntity member, Long id);
}
