package nextstep.reservation;

import nextstep.domain.member.Member;
import nextstep.domain.reservation.Reservation;
import nextstep.domain.reservation.ReservationDao;
import nextstep.domain.schedule.Schedule;
import nextstep.domain.theme.Theme;
import nextstep.dto.request.ReservationRequest;
import nextstep.service.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ReservationServiceTest {

    @Mock
    private ReservationDao reservationDao;

    @Mock
    private ReservationWaitingService reservationWaitingService;

    @Mock
    private MemberService memberService;

    @Mock
    private ScheduleService scheduleService;

    @InjectMocks
    private ReservationService reservationService;

    @Test
    void 예약하려는_스케줄에_예약이_이미_있을_경우_예약_대기를_신청할_수_있다() {
        // given
        Long createdReservationWaitingId = 10L;
        Theme theme = new Theme(1L, "테마 이름", "테마 설명", 22_000);
        Schedule schedule = new Schedule(1L, theme, LocalDate.now(), LocalTime.now());
        Member member = new Member(1L, "username", "password", "name", "010-0000-0000", "USER");
        ReservationRequest reservationRequest = new ReservationRequest(schedule.getId());

        given(reservationDao.existsByScheduleId(any(Long.class)))
                .willReturn(true);
        given(memberService.findById(any(Long.class)))
                .willReturn(member);
        given(scheduleService.findById(any(Long.class)))
                .willReturn(schedule);
        given(reservationWaitingService.createReservationWaiting(any(Member.class), any(Schedule.class)))
                .willReturn(createdReservationWaitingId);

        // when
        Long reservationWaitingId = reservationService.createReservationOrReservationWaiting(member.getId(), reservationRequest);

        // then
        assertThat(reservationWaitingId).isEqualTo(createdReservationWaitingId);
        verify(reservationWaitingService, times(1)).createReservationWaiting(any(Member.class), any(Schedule.class));
        verify(reservationDao, times(0)).save(any(Reservation.class));
    }

    @Test
    void 예약이_없는_스케줄에_대해서_예약_대기를_신청할_경우_바로_예약이_된다() {
        // given
        Long createdReservationId = 5L;
        Theme theme = new Theme(1L, "테마 이름", "테마 설명", 22_000);
        Schedule schedule = new Schedule(1L, theme, LocalDate.now(), LocalTime.now());
        Member member = new Member(1L, "username", "password", "name", "010-0000-0000", "USER");
        ReservationRequest reservationRequest = new ReservationRequest(schedule.getId());

        given(reservationDao.existsByScheduleId(any(Long.class)))
                .willReturn(false);
        given(memberService.findById(any(Long.class)))
                .willReturn(member);
        given(scheduleService.findById(any(Long.class)))
                .willReturn(schedule);
        given(reservationDao.save(any(Reservation.class)))
                .willReturn(createdReservationId);

        // when
        Long reservationId = reservationService.createReservationOrReservationWaiting(member.getId(), reservationRequest);

        // then
        assertThat(reservationId).isEqualTo(createdReservationId);
        verify(reservationDao, times(1)).save(any(Reservation.class));
        verify(reservationWaitingService, times(0)).createReservationWaiting(any(Member.class), any(Schedule.class));
    }

}
