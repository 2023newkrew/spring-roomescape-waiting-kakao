package nextstep.waitingreservation;

import nextstep.member.Member;
import nextstep.reservation.Reservation;
import nextstep.schedule.Schedule;

public class WaitingReservationResponse {
    private Long id;
    private Schedule schedule;
    private Member member;
    private Long waitNum;

    public WaitingReservationResponse() {
    }

    public WaitingReservationResponse(WaitingReservation waitingReservation) {
        this.id = waitingReservation.getId();
        this.schedule = waitingReservation.getSchedule();
        this.member = waitingReservation.getMember();
        this.waitNum = waitingReservation.getWaitNum();
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
}
