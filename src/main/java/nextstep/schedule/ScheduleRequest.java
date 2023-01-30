package nextstep.schedule;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import nextstep.theme.Theme;

import java.time.LocalDate;
import java.time.LocalTime;

public class ScheduleRequest {
    @Min(1L)
    @NotNull
    private Long themeId;
    @NotNull
    @JsonFormat(shape = Shape.STRING, pattern = "yyyy-MM-dd")
    private String date;
    @NotNull
    @JsonFormat(shape = Shape.STRING, pattern = "HH:mm")
    private String time;

    public ScheduleRequest() {
    }

    public ScheduleRequest(Long themeId, String date, String time) {
        this.themeId = themeId;
        this.date = date;
        this.time = time;
    }

    public Long getThemeId() {
        return themeId;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public Schedule toEntity(Theme theme) {
        return new Schedule(
                theme,
                LocalDate.parse(this.date),
                LocalTime.parse(this.time)
        );
    }
}
