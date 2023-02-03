package nextstep.reservation_waiting;

import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.member.Member;
import nextstep.schedule.Schedule;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationWaiting {

    private Long id;
    private Schedule schedule;
    private Member member;

    public ReservationWaiting(Schedule schedule, Member member) {
        this.schedule = schedule;
        this.member = member;
    }

    public boolean sameMember(final Member member) {
        return member != null && Objects.equals(this.member.getId(), member.getId());
    }
}
