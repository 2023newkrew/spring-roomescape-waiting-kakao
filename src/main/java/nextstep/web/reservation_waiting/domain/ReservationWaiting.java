package nextstep.web.reservation_waiting.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.web.member.domain.Member;
import nextstep.web.schedule.domain.Schedule;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReservationWaiting {
    private Long id;
    private Schedule schedule;
    private Member member;
    private Integer waitNum;

    public ReservationWaiting(Schedule schedule, Member member, Integer waitNum) {
        this.schedule = schedule;
        this.member = member;
        this.waitNum = waitNum;
    }
}
