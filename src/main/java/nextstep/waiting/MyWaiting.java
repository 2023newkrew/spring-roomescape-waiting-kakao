package nextstep.waiting;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.schedule.Schedule;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class MyWaiting {
    private Long id;
    private Schedule schedule;
    private Integer waitNum;
}
