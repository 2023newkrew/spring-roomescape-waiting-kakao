package nextstep.reservation_waiting;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.schedule.Schedule;


@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReservationWaitingResponse {

    private Long id;
    private Schedule schedule;
    private int waitNum;

    public ReservationWaitingResponse(ReservationWaiting reservationWaiting) {
        this.id = reservationWaiting.getReservation().getId();
        this.schedule = reservationWaiting.getReservation().getSchedule();
        this.waitNum = reservationWaiting.getWaitNum();
    }

}
