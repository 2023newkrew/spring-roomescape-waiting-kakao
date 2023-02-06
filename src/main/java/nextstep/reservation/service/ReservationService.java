package nextstep.reservation.service;

import nextstep.member.domain.Member;
import nextstep.reservation.domain.Reservation;
import nextstep.schedule.domain.Schedule;

import java.util.List;

public interface ReservationService {

    Reservation create(Reservation reservation);

    boolean existsBySchedule(Schedule schedule);

    Reservation getById(Long id);

    List<Reservation> getAllByMember(Member member);

    boolean deleteById(Member member, Long id);
}
