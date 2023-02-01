package com.nextstep.interfaces.schedule.dtos;

import lombok.AccessLevel;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@RequiredArgsConstructor
@Data
@Setter(AccessLevel.NONE)
public class ScheduleRequest {

    @Pattern(
            regexp = "^([12]\\d{3}-(0[1-9]|1[0-2])-(0[1-9]|[12]\\d|3[01]))$",
            message = "일자가 형식에 맞지 않습니다."
    )
    @NotNull(message = "일자가 없습니다.")
    private final String date;

    @Pattern(
            regexp = "^(0[0-9]|1[0-9]|2[0-3]):(0[1-9]|[0-5][0-9])$",
            message = "시각이 형식에 맞지 않습니다."
    )
    @NotNull(message = "시각이 없습니다.")
    private final String time;

    @NotNull(message = "테마 ID가 없습니다.")
    private final Long themeId;

    public ScheduleRequest() {
        this(null, null, null);
    }
}
