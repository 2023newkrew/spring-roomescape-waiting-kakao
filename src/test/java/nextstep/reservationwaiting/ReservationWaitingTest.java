package nextstep.reservationwaiting;

import nextstep.schedule.Schedule;
import nextstep.theme.Theme;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThatCode;

class ReservationWaitingTest {

    @Test
    @DisplayName("Reservation Waiting은 id 없이 생성이 가능하다.")
    void create() {
        assertThatCode(() -> new ReservationWaiting(new Schedule(1L, new Theme(1L, "theme", "desc", 123),
                LocalDate.parse("2023-01-26"), LocalTime.parse("13:00:00")), 1L, 1L));
    }

    @Test
    @DisplayName("Reservation Waiting은 id 있이 생성이 가능하다.")
    void createWithId() {
        assertThatCode(() -> new ReservationWaiting(1L, new Schedule(1L, new Theme(1L, "theme", "desc", 123),
                LocalDate.parse("2023-01-26"), LocalTime.parse("13:00:00")), 1L, 1L));
    }

}