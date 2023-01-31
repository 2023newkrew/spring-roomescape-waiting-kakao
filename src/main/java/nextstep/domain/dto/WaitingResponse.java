package nextstep.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.domain.persist.Schedule;
import nextstep.domain.persist.Waiting;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class WaitingResponse {
    private long id;
    private Schedule schedule;
    private long waitNum;

    public WaitingResponse(Waiting waiting, long waitNum) {
        this.id = waiting.getId();
        this.schedule = waiting.getSchedule();
        this.waitNum = waitNum;
    }
}
