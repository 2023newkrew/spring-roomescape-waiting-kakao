package nextstep.reservation;

import nextstep.member.Member;
import nextstep.schedule.Schedule;

public class ReservationResponse {
    private Long id;
    private Schedule schedule;
    private Member member;

    public ReservationResponse(Long id, Schedule schedule, Member member) {
        this.id = id;
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
}