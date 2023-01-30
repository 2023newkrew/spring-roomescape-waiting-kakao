package nextstep.reservationwaiting;

import nextstep.domain.member.Member;
import nextstep.domain.reservationwaiting.ReservationWaiting;
import nextstep.domain.reservationwaiting.ReservationWaitingDao;
import nextstep.domain.schedule.Schedule;
import nextstep.domain.theme.Theme;
import nextstep.error.ApplicationException;
import nextstep.service.ReservationWaitingService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ReservationWaitingTest {

    @Mock
    private ReservationWaitingDao reservationWaitingDao;

    @InjectMocks
    private ReservationWaitingService reservationWaitingService;

    @Test
    void 자신의_예약_대기를_취소할_수_있다() {
        // given
        Theme theme = new Theme(1L, "테마 이름", "테마 설명", 22_000);
        Schedule schedule = new Schedule(1L, theme, LocalDate.now(), LocalTime.now());
        Member member = new Member(1L, "username", "password", "name", "010-0000-0000", "USER");
        ReservationWaiting reservationWaiting = new ReservationWaiting(10L, member, schedule, 10);

        given(reservationWaitingDao.findById(any(Long.class)))
                .willReturn(Optional.of(reservationWaiting));

        // when
        reservationWaitingService.deleteReservationWaitingById(member.getId(), reservationWaiting.getId());

        // then
        verify(reservationWaitingDao, times(1)).deleteById(any(Long.class));
    }

    @Test
    void 자신의_예약_대기가_아닌_경우_취소할_수_없다() {
        // given
        Long wrongMemberId = 5L;
        Theme theme = new Theme(1L, "테마 이름", "테마 설명", 22_000);
        Schedule schedule = new Schedule(1L, theme, LocalDate.now(), LocalTime.now());
        Member member = new Member(1L, "username", "password", "name", "010-0000-0000", "USER");
        ReservationWaiting reservationWaiting = new ReservationWaiting(10L, member, schedule, 10);

        given(reservationWaitingDao.findById(any(Long.class)))
                .willReturn(Optional.of(reservationWaiting));

        // when, then
        assertThatThrownBy(() -> reservationWaitingService.deleteReservationWaitingById(wrongMemberId, reservationWaiting.getId()))
                .isInstanceOf(ApplicationException.class);
    }


}
