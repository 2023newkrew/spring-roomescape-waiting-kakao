package roomescape.entity;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;


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
    private Reservation.Status status;
    @NotNull
    private Theme theme;

    public enum Status {
        Unapproved(0),
        Approved(1),
        CancelRequested(2),
        Canceled(3),
        Disapprove(4);

        private static final Map<Integer, Status> ALL_CANDIDATES = Arrays.stream(Status.values())
                                                                         .collect(Collectors.toMap(
                                                                                 v -> v.code,
                                                                                 v -> v
                                                                         ));
        private final int code;

        Status(int code) {
            this.code = code;
        }

        public static Status from(int value) {
            if (!ALL_CANDIDATES.containsKey(value)) {
                throw new IllegalArgumentException();
            }
            return ALL_CANDIDATES.get(value);
        }

        @JsonValue
        public int into() {
            return code;
        }
    }
}
