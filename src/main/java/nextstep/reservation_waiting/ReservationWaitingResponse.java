package nextstep.reservation_waiting;

import lombok.AllArgsConstructor;
import lombok.Getter;
import nextstep.schedule.Schedule;

@Getter
@AllArgsConstructor
public class ReservationWaitingResponse {
    private Long id;
    private Schedule schedule;
    private Integer waitNum;

    public static ReservationWaitingResponse fromDomain(ReservationWaiting reservationWaiting){
        return new ReservationWaitingResponse(
                reservationWaiting.getId(),
                reservationWaiting.getSchedule(),
                reservationWaiting.getWaitNum()
        );
    }
}
