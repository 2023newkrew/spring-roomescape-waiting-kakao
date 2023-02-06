package nextstep.schedule.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ScheduleRequest {

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "일자가 없습니다.")
    private LocalDate date;

    @DateTimeFormat(pattern = "HH:mm")
    @NotNull(message = "시각이 없습니다.")
    private LocalTime time;

    @NotNull(message = "테마 ID가 없습니다.")
    private Long themeId;
}
