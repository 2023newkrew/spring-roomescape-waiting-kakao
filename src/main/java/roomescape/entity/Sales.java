package roomescape.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Sales {
    @NotNull
    private LocalDateTime createdAt;
    @NotNull
    private String reason;
    @NotNull
    private BigDecimal change;
    private Long reservationId;
}
