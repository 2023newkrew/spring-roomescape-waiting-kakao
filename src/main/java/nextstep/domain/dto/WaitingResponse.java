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
    private Long id;
    private Schedule schedule;
    private Long waitNum;

    public WaitingResponse(Waiting waiting, Long waitNum) {
        this.id = waiting.getId();
        this.schedule = waiting.getSchedule();
        this.waitNum = waitNum;
    }
}
