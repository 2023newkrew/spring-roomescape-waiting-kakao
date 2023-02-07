package nextstep.waiting.service;

import nextstep.member.domain.Member;
import nextstep.reservation.domain.Reservation;
import nextstep.waiting.domain.Waiting;

import java.util.List;

public interface WaitingService {
    Waiting create(Reservation reservation);

    Waiting getById(Long id);

    List<Waiting> getAllByMember(Member member);

    boolean deleteById(Member member, Long id);
}
