package roomescape.controller.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import roomescape.entity.Waiting;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class WaitingControllerGetResponse {
    private List<Element> items;

    public static WaitingControllerGetResponse from(List<Waiting> waitings) {
        return new WaitingControllerGetResponse(
                waitings
                        .stream()
                        .map(reservation -> new Element(
                                reservation.getId(),
                                reservation.getDate(),
                                reservation.getTime(),
                                reservation.getName(),
                                reservation.getTheme().getId(),
                                reservation.getTheme().getName(),
                                reservation.getTheme().getDesc(),
                                reservation.getTheme().getPrice(),
                                reservation.getWaitingNumber()
                        ))
                        .toList()
        );
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Element {

        @NotNull
        private Long id;

        @NotNull
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        private LocalDate date;

        @NotNull
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "HH:mm")
        @DateTimeFormat(pattern = "HH:mm")
        private LocalTime time;

        @NotBlank
        private String name;

        @JsonProperty("theme_id")
        @NotBlank
        private Long themeId;

        @JsonProperty("theme_name")
        @NotBlank
        private String themeName;

        @JsonProperty("theme_desc")
        @NotNull
        private String themeDesc;

        @JsonProperty("theme_price")
        @NotNull
        @PositiveOrZero
        private Integer themePrice;

        @JsonProperty("wait_number")
        @NotNull
        @Positive
        private Integer waitNumber;
    }
}
