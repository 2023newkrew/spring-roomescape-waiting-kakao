package roomescape.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ThemeControllerPostBody {
    @NotBlank
    private String name;

    @NotBlank
    private String desc;

    @NotNull
    @PositiveOrZero
    private Integer price;
}
