package nextstep.schedule;

import java.time.LocalDate;
import java.time.LocalTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.theme.Theme;

@Getter
@NoArgsConstructor
@AllArgsConstructor
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
