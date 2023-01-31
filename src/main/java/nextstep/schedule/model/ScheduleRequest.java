package nextstep.schedule.model;

import lombok.*;
import nextstep.theme.model.Theme;

import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@ToString
public class ScheduleRequest {
    private Long themeId;
    @NotBlank
    private String date;

    @NotBlank
    private String time;

    public ScheduleRequest() {
    }

    public ScheduleRequest(Long themeId, String date, String time) {
        this.themeId = themeId;
        this.date = date;
        this.time = time;
    }

    public Schedule toEntity(Theme theme) {
        return new Schedule(
                theme,
                LocalDate.parse(this.date),
                LocalTime.parse(this.time)
        );
    }
}
