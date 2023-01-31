package nextstep.reservation.model;

import lombok.*;

@Getter
@ToString
public class CreateReservation {
    private final Long scheduleId;

    private final String memberName;

    public CreateReservation(Long scheduleId, String memberName){
        this.memberName = memberName;
        this.scheduleId = scheduleId;
    }

    public static CreateReservation toCreateEntity(ReservationRequest reservationRequest, String memberName){
        return new CreateReservation(reservationRequest.getScheduleId(), memberName);
    }
}
