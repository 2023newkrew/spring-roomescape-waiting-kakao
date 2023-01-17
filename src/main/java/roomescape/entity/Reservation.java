package roomescape.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;


@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Reservation {
    @NotNull
    private Long id;

    @NotNull
    private LocalDate date;

    @NotNull
    private LocalTime time;

    @NotBlank
    private String name;

    @NotNull
    private Long memberId;

    @NotNull
    private Theme theme;
}
