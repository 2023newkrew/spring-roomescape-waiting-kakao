package nextstep.reservation.repository;

import nextstep.member.domain.Member;
import nextstep.reservation.domain.Reservation;
import nextstep.schedule.domain.Schedule;

import java.util.List;

public interface ReservationRepository {

    boolean existsBySchedule(Schedule schedule);

    boolean existsByMemberAndSchedule(Reservation reservation);

    Reservation insert(Reservation reservation);

    Reservation getById(Long id);

    List<Reservation> getByMember(Member member);

    boolean updateById(Long id, Long memberId);

    boolean deleteById(Long id);
}
