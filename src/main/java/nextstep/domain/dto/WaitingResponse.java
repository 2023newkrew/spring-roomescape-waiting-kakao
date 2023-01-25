package nextstep.domain.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import nextstep.domain.persist.Schedule;
import nextstep.domain.persist.Waiting;

import java.util.Objects;

@Getter
@AllArgsConstructor
@RequiredArgsConstructor
public class WaitingResponse {
    private Long id;
    private Schedule schedule;
    private Long waitNum;

    public WaitingResponse(Waiting waiting, long waitNum) {
        this.id = waiting.getId();
        this.schedule = waiting.getSchedule();
        this.waitNum = waitNum;
    }
}
