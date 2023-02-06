package nextstep.controller.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import nextstep.domain.Schedule;
import nextstep.domain.Theme;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@RequiredArgsConstructor
@AllArgsConstructor
public class ScheduleRequest {
    private long themeId;
    private String date;
    private String time;

    public Schedule toEntity(Theme theme) {
        return new Schedule(
                theme,
                LocalDate.parse(this.date),
                LocalTime.parse(this.time)
        );
    }
}
