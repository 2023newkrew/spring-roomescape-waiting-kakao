package nextstep.reservation.model;

import lombok.*;

@Getter
@ToString
public class CreateReservationRequest {
    private final Long scheduleId;

    private final String memberName;

    public CreateReservationRequest(Long scheduleId, String memberName){
        this.memberName = memberName;
        this.scheduleId = scheduleId;
    }

    public static CreateReservationRequest to(ReservationRequest reservationRequest, String memberName){
        return new CreateReservationRequest(reservationRequest.getScheduleId(), memberName);
    }
}
