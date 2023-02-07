package roomescape.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Theme {
    @NotNull
    private Long id;

    @NotBlank
    private String name;

    @NotNull
    private String desc;

    @NotNull
    @PositiveOrZero
    private Integer price;
}
