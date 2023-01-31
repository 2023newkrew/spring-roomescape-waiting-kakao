package nextstep.reservation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Setter;
import nextstep.member.Member;
import nextstep.schedule.Schedule;

import java.time.LocalDateTime;
import java.util.Objects;

@AllArgsConstructor
@Builder
@Setter
public class Reservation {
    private Long id;
    private Schedule schedule;
    private Member member;
    private LocalDateTime createdDateTime;

    public Reservation() {
    }

    public Reservation(Schedule schedule, Member member) {
        this.schedule = schedule;
        this.member = member;
    }

    public Long getId() {
        return id;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public Member getMember() {
        return member;
    }

    public boolean sameMember(Member member) {
        return member != null && Objects.equals(this.member.getId(), member.getId());
    }
}
