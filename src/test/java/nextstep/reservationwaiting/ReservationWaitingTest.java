package nextstep.reservationwaiting;

import nextstep.schedule.Schedule;
import nextstep.theme.Theme;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThatCode;

class ReservationWaitingTest {
    Schedule schedule;

    @BeforeEach
    void setUp() {
        schedule = new Schedule(
                1L,
                new Theme(1L, "theme", "desc", 123),
                LocalDate.parse("2023-01-26"),
                LocalTime.parse("13:00:00")
        );
    }

    @Test
    @DisplayName("Reservation Waiting 은 id 없이도 생성이 가능하다.")
    void create() {
        assertThatCode(() -> new ReservationWaiting(schedule, 1L, 1L)).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("Reservation Waiting 이 정상적으로 생성된다.")
    void createWithId() {
        assertThatCode(() -> new ReservationWaiting(1L, schedule, 1L, 1L)).doesNotThrowAnyException();
    }

}