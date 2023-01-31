package nextstep.web.schedule;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.web.theme.Theme;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleRequest {
    private Long themeId;
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
