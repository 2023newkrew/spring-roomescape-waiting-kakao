package nextstep.schedule;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import nextstep.theme.Theme;

import java.time.LocalDate;
import java.time.LocalTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
public class ScheduleRequest {
    private Long themeId;
    private String date;
    private String time;

    public Schedule toEntity(Theme theme) {
        return Schedule.builder()
                .theme(theme)
                .date(LocalDate.parse(this.date))
                .time(LocalTime.parse(this.time))
                .build();
    }
}
