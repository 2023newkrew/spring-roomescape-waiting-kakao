package nextstep.schedule.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;


@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ScheduleSearchRequest {

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "일자가 없습니다.")
    private LocalDate date;

    @NotNull
    private Long themeId;
}
