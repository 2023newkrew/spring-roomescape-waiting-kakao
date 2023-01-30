package nextstep.waiting;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import nextstep.schedule.Schedule;

@RequiredArgsConstructor
@Getter
public class MyWaiting {
    private final Long id;
    private final Schedule schedule;
    private final Integer waitNum;
}
