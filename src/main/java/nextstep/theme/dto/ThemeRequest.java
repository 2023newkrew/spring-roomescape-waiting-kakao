package nextstep.theme.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;

@NoArgsConstructor
@AllArgsConstructor
@Getter
public class ThemeRequest {

    @NotBlank(message = "이름은 공백일 수 없습니다.")
    private String name;

    private String desc;

    @Min(value = 1, message = "가격은 1원 이상입니다.")
    private Integer price;
}
