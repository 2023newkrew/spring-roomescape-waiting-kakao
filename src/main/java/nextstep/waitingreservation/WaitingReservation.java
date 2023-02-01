package nextstep.waitingreservation;

import auth.UserDetails;
import nextstep.member.Member;
import nextstep.schedule.Schedule;

import java.util.Objects;

public class WaitingReservation {
    private Long id;
    private Schedule schedule;
    private Member member;
    private Long waitNum;

    public WaitingReservation() {
    }

    public WaitingReservation(Schedule schedule, Member member, Long waitNum) {
        this.schedule = schedule;
        this.member = member;
        this.waitNum = waitNum;
    }

    public WaitingReservation(Long id, Schedule schedule, Member member, Long waitNum) {
        this.id = id;
        this.schedule = schedule;
        this.member = member;
        this.waitNum = waitNum;
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

    public Long getWaitNum() {
        return waitNum;
    }

    public boolean sameMember(UserDetails userDetails) {
        return Objects.equals(this.member.getId(), userDetails.getId());
    }
}
