package nextstep.reservation;

import nextstep.schedule.Schedule;
import nextstep.schedule.ScheduleResponse;

import java.util.List;
import java.util.stream.Collectors;

public class ReservationResponse {
    private final Long id;
    private final ScheduleResponse schedule;

    public ReservationResponse(Long id, ScheduleResponse schedule) {
        this.id = id;
        this.schedule = schedule;
    }

    public Long getId() {
        return id;
    }

    public ScheduleResponse getSchedule() {
        return schedule;
    }

    public static List<ReservationResponse> from(List<Reservation> reservations) {
        return reservations.stream()
                .map(ReservationResponse::from)
                .collect(Collectors.toList());
    }

    public static ReservationResponse from(Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(),
                ScheduleResponse.from(reservation.getSchedule())
        );
    }
}
