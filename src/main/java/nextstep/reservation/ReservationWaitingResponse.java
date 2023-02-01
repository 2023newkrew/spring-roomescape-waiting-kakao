package nextstep.reservation;

import nextstep.member.Member;
import nextstep.schedule.Schedule;
import nextstep.waitingreservation.WaitingReservation;

public class ReservationWaitingResponse {
    private Long id;
    private Schedule schedule;
    private Member member;
    private Long waitNum;

    public ReservationWaitingResponse() {
    }

    public ReservationWaitingResponse(WaitingReservation waitingReservation) {
        this.id = waitingReservation.getId();
        this.schedule = waitingReservation.getSchedule();
        this.member = waitingReservation.getMember();
        this.waitNum = waitingReservation.getWaitNum();
    }

    // TODO: remove later
    public ReservationWaitingResponse(Reservation reservation) {
        this.id = reservation.getId();
        this.schedule = reservation.getSchedule();
        this.member = reservation.getMember();
        this.waitNum = reservation.getWaitNum();
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
