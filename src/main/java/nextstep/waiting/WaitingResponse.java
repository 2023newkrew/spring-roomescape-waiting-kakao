package nextstep.waiting;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.schedule.Schedule;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class WaitingResponse {
    private Long id;
    private Schedule schedule;
    private int waitNum;

    public static WaitingResponse from(Waiting waiting, int waitNum) {
        return new WaitingResponse(waiting.getId(), waiting.getSchedule(), waitNum);
    }
}
