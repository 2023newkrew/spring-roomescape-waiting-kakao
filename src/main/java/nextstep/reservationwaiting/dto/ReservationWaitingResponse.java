package nextstep.reservationwaiting.dto;

import nextstep.reservationwaiting.domain.ReservationWaiting;
import nextstep.schedule.domain.Schedule;

import java.time.LocalDateTime;

public class ReservationWaitingResponse {
    private Long id;
    private Schedule schedule;
    private String waitNum;

    private ReservationWaitingResponse(Long id, Schedule schedule, String waitNum) {
        this.id = id;
        this.schedule = schedule;
        this.waitNum= waitNum;
    }

    public static ReservationWaitingResponse of(ReservationWaiting reservationWaiting, String waitNum){
        return new ReservationWaitingResponse(
                reservationWaiting.getId(),
                reservationWaiting.getSchedule(),
                waitNum
        );
    }

    public Long getId() {
        return id;
    }

    public Schedule getSchedule() {
        return schedule;
    }

    public String getWaitNum() {
        return waitNum;
    }
}
