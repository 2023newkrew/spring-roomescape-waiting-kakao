package nextstep.theme.dto;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@RequiredArgsConstructor
@Getter
public class ThemeRequest {

    @NotBlank(message = "이름은 공백일 수 없습니다.")
    private final String name;

    private final String desc;

    @Min(value = 1, message = "가격은 1원 이상입니다.")
    private final Integer price;

    ThemeRequest() {
        this(null, null, null);
    }
}
