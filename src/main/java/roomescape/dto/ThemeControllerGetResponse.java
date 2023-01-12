package roomescape.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ThemeControllerGetResponse {
    @NotNull
    private Long id;

    @NotNull
    private String name;

    @NotNull
    private String desc;

    @NotNull
    @PositiveOrZero
    private Integer price;
}
