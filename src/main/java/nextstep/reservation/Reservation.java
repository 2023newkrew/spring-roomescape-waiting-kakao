package nextstep.reservation;

import auth.UserDetails;
import nextstep.member.Member;
import nextstep.schedule.Schedule;

import java.util.Objects;

public class Reservation {
    private Long id;
    private Schedule schedule;
    private Member member;
    private Long waitNum;

    public Reservation() {
    }

    public Reservation(Schedule schedule, Member member, Long waitNum) {
        this.schedule = schedule;
        this.member = member;
        this.waitNum = waitNum;
    }

    public Reservation(Long id, Schedule schedule, Member member, Long waitNum) {
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

    public boolean sameMember(Member member) {
        return member != null && Objects.equals(this.member.getId(), member.getId());
    }

    public boolean sameMember(UserDetails userDetails) {
        return userDetails != null && Objects.equals(this.member.getId(), userDetails.getId());
    }
}
