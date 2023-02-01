package nextstep.domain.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.domain.persist.Schedule;
import nextstep.domain.persist.Waiting;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class WaitingResponse {
    @Schema(description = "예약 대기 id")
    private Long id;
    @Schema(description = "대기된 스케줄 정보")
    private ScheduleResponse schedule;
    @Schema(description = "대기 번호")
    private Long waitNum;

    public WaitingResponse(Waiting waiting, Long waitNum) {
        this.id = waiting.getId();
        this.schedule = new ScheduleResponse(waiting.getSchedule());
        this.waitNum = waitNum;
    }
}
