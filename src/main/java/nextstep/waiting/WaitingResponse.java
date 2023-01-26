package nextstep.waiting;

import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.schedule.Schedule;

@Getter
@NoArgsConstructor
public class WaitingResponse {
    private Long id;
    private Schedule schedule;
    private int waitNum;

    public WaitingResponse(Waiting waiting, int waitNum) {
        this.id = waiting.getId();
        this.schedule = waiting.getSchedule();
        this.waitNum = waitNum;
    }
}
