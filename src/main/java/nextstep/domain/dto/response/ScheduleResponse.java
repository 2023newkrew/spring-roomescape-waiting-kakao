package nextstep.domain.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import nextstep.domain.persist.Schedule;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@AllArgsConstructor
public class ScheduleResponse {
    @Schema(description = "스케줄 id")
    private Long id;
    @Schema(description = "스케줄에 담긴 테마 정보")
    private ThemeResponse theme;
    @Schema(description = "스케줄 날짜")
    private LocalDate date;
    @Schema(description = "스케줄 시간")
    private LocalTime time;

    public ScheduleResponse(Schedule schedule) {
        this.id = schedule.getId();
        this.theme = new ThemeResponse(schedule.getTheme());
        this.date = schedule.getDate();
        this.time = schedule.getTime();
    }
}
