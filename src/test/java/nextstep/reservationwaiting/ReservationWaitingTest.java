package nextstep.reservationwaiting;

import static org.assertj.core.api.Assertions.assertThatCode;

import java.time.LocalDate;
import java.time.LocalTime;
import nextstep.schedule.Schedule;
import nextstep.theme.Theme;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ReservationWaitingTest {

    private final Schedule schedule = Schedule.giveId(Schedule
            .builder()
            .time(LocalTime.parse("13:00:00"))
            .date(LocalDate.parse("2023-01-26"))
            .theme(new Theme(1L, "theme", "desc", 123))
            .build(), 1L);

    @Test
    @DisplayName("Reservation Waiting은 id 없이 생성이 가능하다.")
    void create() {
        assertThatCode(() -> ReservationWaiting.builder()
                .waitNum(1L)
                .memberId(1L)
                .schedule(schedule)
                .build()).doesNotThrowAnyException();

    }

    @Test
    @DisplayName("Reservation Waiting은 id 있이 생성이 가능하다.")
    void createWithId() {
        assertThatCode(() -> ReservationWaiting.giveId(
                ReservationWaiting.builder()
                        .schedule(schedule)
                        .waitNum(1L)
                        .memberId(1L).build(),
                1L)).doesNotThrowAnyException();
    }

}