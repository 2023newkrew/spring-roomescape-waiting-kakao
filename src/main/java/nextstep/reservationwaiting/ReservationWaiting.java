package nextstep.reservationwaiting;

import nextstep.member.Member;
import nextstep.schedule.Schedule;

import java.util.Objects;

public class ReservationWaiting {
    private Long id;
    private Long scheduleId;
    private Long memberId;
    private int waitNum;

    public ReservationWaiting() {
    }

    public ReservationWaiting(Long scheduleId, Long memberId, int waitNum) {
        this.scheduleId = scheduleId;
        this.memberId = memberId;
        this.waitNum = waitNum;
    }

    public ReservationWaiting(Long id, Long scheduleId, Long memberId, int waitNum) {
        this.id = id;
        this.scheduleId = scheduleId;
        this.memberId = memberId;
        this.waitNum= waitNum;
    }

    public Long getId() {
        return id;
    }

    public Long getScheduleId() {
        return scheduleId;
    }

    public Long getMemberId() {
        return memberId;
    }

    public int getWaitNum() {
        return waitNum;
    }

    public boolean sameMember(Member member) {
        return member != null && Objects.equals(this.memberId, member.getId());
    }
}
