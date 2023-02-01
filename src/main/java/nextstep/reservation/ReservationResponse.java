package nextstep.reservation;

import nextstep.schedule.Schedule;

import java.util.List;
import java.util.stream.Collectors;

public class ReservationResponse {
    private final Long id;
    private final Schedule schedule;

    public ReservationResponse(Long id, Schedule schedule) {
        this.id = id;
        this.schedule = schedule;
    }

    public static ReservationResponse of(Reservation reservation) {
        return new ReservationResponse(
                reservation.getId(),
                reservation.getSchedule()
        );
    }

    public static List<ReservationResponse> toList(List<Reservation> reservations) {
        return reservations.stream()
                .map(ReservationResponse::of)
                .collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public Schedule getSchedule() {
        return schedule;
    }
}
