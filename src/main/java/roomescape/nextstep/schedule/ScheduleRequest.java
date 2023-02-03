package roomescape.nextstep.schedule;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import roomescape.nextstep.theme.Theme;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleRequest {
    @PositiveOrZero
    private Long themeId;
    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}")
    private String date;
    @Pattern(regexp = "\\d{2}:\\d{2}")
    private String time;

    public Schedule toEntity(Theme theme) {
        return new Schedule(
                theme,
                LocalDate.parse(this.date),
                LocalTime.parse(this.time)
        );
    }
}
