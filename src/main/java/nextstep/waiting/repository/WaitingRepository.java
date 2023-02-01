package nextstep.waiting.repository;

import nextstep.member.domain.MemberEntity;
import nextstep.schedule.domain.ScheduleEntity;
import nextstep.waiting.domain.WaitingEntity;

import java.util.List;

public interface WaitingRepository {

    WaitingEntity insert(WaitingEntity waiting);

    WaitingEntity getById(Long id);

    WaitingEntity getFirstBySchedule(ScheduleEntity schedule);

    List<WaitingEntity> getByMember(MemberEntity member);

    boolean deleteById(Long id);
}
