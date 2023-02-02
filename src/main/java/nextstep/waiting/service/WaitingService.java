package nextstep.waiting.service;

import nextstep.member.domain.MemberEntity;
import nextstep.reservation.domain.Reservation;
import nextstep.reservation.domain.ReservationEntity;
import nextstep.waiting.domain.WaitingEntity;

import java.util.List;

public interface WaitingService {
    WaitingEntity create(Reservation reservation);

    WaitingEntity getById(Long id);

    List<WaitingEntity> getByMember(MemberEntity member);

    boolean deleteById(MemberEntity member, Long id);
    
    void onReservationDeleted(ReservationEntity reservation);
}
