package nextstep.exception;

import nextstep.member.Member;
import nextstep.reservation.dto.ReservationRequest;
import nextstep.reservationwaiting.dto.ReservationWaitingRequest;

public class CanMakeReservationException extends RuntimeException {
    private final Member member;
    private final ReservationRequest request;

    public CanMakeReservationException(Member member, ReservationWaitingRequest request) {
        this.member = member;
        this.request = new ReservationRequest(request.getScheduleId());
    }

    public Member getMember() {
        return member;
    }

    public ReservationRequest getRequest() {
        return request;
    }
}
