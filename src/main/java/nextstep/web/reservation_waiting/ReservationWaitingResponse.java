package nextstep.web.reservation_waiting;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.web.schedule.Schedule;

@Getter
@AllArgsConstructor
@NoArgsConstructor
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
