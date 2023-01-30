package nextstep.reservation;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.schedule.Schedule;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class MyReservation {
    private Long id;
    private Schedule schedule;

}
