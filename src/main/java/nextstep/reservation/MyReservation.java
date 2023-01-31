package nextstep.reservation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import nextstep.schedule.Schedule;

@RequiredArgsConstructor
@Getter
public class MyReservation {
    private final Long id;
    private final Schedule schedule;

}
