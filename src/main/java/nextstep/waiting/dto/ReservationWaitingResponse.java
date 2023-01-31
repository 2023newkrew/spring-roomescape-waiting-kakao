package nextstep.waiting.dto;

import nextstep.schedule.Schedule;
import nextstep.waiting.ReservationWaiting;

public class ReservationWaitingResponse {
    private Long id;
    private Long waitingNumber;
    private Schedule schedule;

    private ReservationWaitingResponse(Long id, Long waitingNumber, Schedule schedule) {
        this.id = id;
        this.waitingNumber = waitingNumber;
        this.schedule = schedule;
    }
    public static ReservationWaitingResponse of(ReservationWaiting reservationWaiting) {
        return new ReservationWaitingResponse(
                reservationWaiting.getId(),
                reservationWaiting.getWaitingNumber(),
                reservationWaiting.getSchedule()
        );
    }

    public Long getId() {
        return id;
    }

    public Long getWaitingNumber() {
        return waitingNumber;
    }

    public Schedule getSchedule() {
        return schedule;
    }
}
