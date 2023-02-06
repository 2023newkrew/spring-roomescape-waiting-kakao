package nextstep.reservation.service;

import nextstep.member.domain.Member;
import nextstep.reservation.domain.Reservation;

import java.util.List;

public interface ReservationService {

    Reservation create(Reservation reservation);

    Reservation getById(Long id);

    List<Reservation> getByMember(Member member);

    boolean deleteById(Member member, Long id);
}
