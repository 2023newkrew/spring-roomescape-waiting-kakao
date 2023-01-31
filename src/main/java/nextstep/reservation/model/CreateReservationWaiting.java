package nextstep.reservation.model;

import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
public class CreateReservationWaiting {
    private final Long scheduleId;

    private final Long memberId;
    private final String memberName;

    public CreateReservationWaiting(Long scheduleId, Long memberId, String memberName){
        this.scheduleId = scheduleId;
        this.memberId = memberId;
        this.memberName = memberName;
    }

    public static CreateReservationWaiting from(ReservationWaitingRequest reservationRequest, Long memberId, String memberName){
        return new CreateReservationWaiting(reservationRequest.getScheduleId(), memberId, memberName);
    }
}
