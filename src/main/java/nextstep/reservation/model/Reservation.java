package nextstep.reservation.model;

import lombok.Getter;
import lombok.ToString;
import nextstep.member.model.Member;
import nextstep.schedule.model.Schedule;

@Getter
@ToString
public class Reservation {
    private Long id;
    private Schedule schedule;
    private String memberName;

    public Reservation() {
    }

    public Reservation(Schedule schedule, String memberName) {
        this.schedule = schedule;
        this.memberName = memberName;
    }

    public Reservation(Long id, Schedule schedule, String memberName) {
        this.id = id;
        this.schedule = schedule;
        this.memberName = memberName;
    }

    public boolean isReservedBy(Member member){
        return this.memberName.equals(member.getMemberName());
    }
}
