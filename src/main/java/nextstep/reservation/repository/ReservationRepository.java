package nextstep.reservation.repository;

import nextstep.member.domain.MemberEntity;
import nextstep.reservation.domain.ReservationEntity;
import nextstep.schedule.domain.ScheduleEntity;

import java.util.List;

public interface ReservationRepository {

    boolean existsBySchedule(ScheduleEntity schedule);

    boolean existsByMemberAndSchedule(ReservationEntity reservation);

    ReservationEntity insert(ReservationEntity reservation);

    ReservationEntity getById(Long id);

    List<ReservationEntity> getByMember(MemberEntity member);

    boolean updateById(Long id, Long memberId);

    boolean deleteById(Long id);
}
