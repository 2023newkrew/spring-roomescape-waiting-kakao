package nextstep.reservation.dto;

import lombok.RequiredArgsConstructor;
import nextstep.reservation.response.ReservationResponse;
import nextstep.reservation.response.ReservationWaitingResponse;
import nextstep.schedule.Schedule;

@RequiredArgsConstructor
public class ReservationReadDto {
    private final Long reservationId;
    private final Schedule schedule;
    private final Long waitNum;

    public ReservationResponse toReservationResponse() {
        ReservationResponse reservationResponse = new ReservationResponse();
        reservationResponse.setId(reservationId);
        reservationResponse.setSchedule(schedule);
        return reservationResponse;
    }

    public ReservationWaitingResponse toReservationWaitingResponse() {
        ReservationWaitingResponse reservationWaitingResponse = new ReservationWaitingResponse();
        reservationWaitingResponse.setId(reservationId);
        reservationWaitingResponse.setSchedule(schedule);
        reservationWaitingResponse.setWaitNum(waitNum);
        return reservationWaitingResponse;
    }
}
