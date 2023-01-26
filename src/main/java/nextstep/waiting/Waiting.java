package nextstep.waiting;

import lombok.Getter;
import nextstep.member.Member;
import nextstep.schedule.Schedule;

@Getter
public class Waiting {
    private Long id;
    private Schedule schedule;
    private Member member;
}
