package nextstep.schedule.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;


@RequiredArgsConstructor
@Data
@Setter(AccessLevel.NONE)
public class ScheduleSearchRequest {

    @Pattern(
            regexp = "^([12]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01]))$",
            message = "일자가 형식에 맞지 않습니다."
    )
    private final String date;

    @NotNull
    private final Long themeId;

    ScheduleSearchRequest() {
        this(null, null);
    }
}
