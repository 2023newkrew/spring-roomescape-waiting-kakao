package nextstep.waiting.repository;

import nextstep.member.domain.Member;
import nextstep.schedule.domain.Schedule;
import nextstep.waiting.domain.Waiting;

import java.util.List;

public interface WaitingRepository {

    Waiting insert(Waiting waiting);

    Waiting getById(Long id);

    Waiting getFirstBySchedule(Schedule schedule);

    List<Waiting> getByMember(Member member);

    boolean deleteById(Long id);
}
