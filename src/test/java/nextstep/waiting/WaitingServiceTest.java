package nextstep.waiting;

import nextstep.member.Member;
import nextstep.reservation.Reservation;
import nextstep.reservation.ReservationDao;
import nextstep.schedule.Schedule;
import nextstep.schedule.ScheduleDao;
import nextstep.waiting.dto.response.ReservationWaitingResponseDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class WaitingServiceTest {

    @Mock
    private ReservationDao reservationDao;

    @Mock
    private ScheduleDao scheduleDao;

    @InjectMocks
    private WaitingService waitingService;

    @Test
    @DisplayName("유효한 요청일 경우 예약 대기 생성은 성공해야 한다.")
    void should_createWaitingSuccessfully_when_validValidParameter() {
        Long scheduleId = 1L;
        Long newReservationId = 1L;
        when(scheduleDao.findById(scheduleId)).thenReturn(Optional.of(mock(Schedule.class)));
        when(reservationDao.save(any(Reservation.class))).thenReturn(newReservationId);
        Assertions.assertDoesNotThrow(() -> waitingService.createWaiting(mock(Member.class), scheduleId));
    }

    @Test
    @DisplayName("많은 예약들 속에서 해당 회원의 예약 대기만 가져와야 한다.")
    void should_getReservationWaitingsByMember_when_manyReservations() {
        List<Reservation> reservationsByMemberId = List.of(
                new Reservation(1L, mock(Schedule.class), mock(Member.class), 0L, Reservation.Status.WAITING_APPROVAL),
                new Reservation(2L, mock(Schedule.class), mock(Member.class), 1L, Reservation.Status.WAITING_APPROVAL),
                new Reservation(3L, mock(Schedule.class), mock(Member.class), 2L, Reservation.Status.WAITING_APPROVAL),
                new Reservation(4L, mock(Schedule.class), mock(Member.class), 3L, Reservation.Status.WAITING_APPROVAL),
                new Reservation(5L, mock(Schedule.class), mock(Member.class), 4L, Reservation.Status.WAITING_APPROVAL)
        );
        when(reservationDao.getPriority(any(), eq(0L))).thenReturn(0L);
        when(reservationDao.getPriority(any(), eq(1L))).thenReturn(0L);
        when(reservationDao.getPriority(any(), eq(2L))).thenReturn(1L);
        when(reservationDao.getPriority(any(), eq(3L))).thenReturn(3L);
        when(reservationDao.getPriority(any(), eq(4L))).thenReturn(3L);
        when(reservationDao.findByMemberId(anyLong())).thenReturn(reservationsByMemberId);

        List<ReservationWaitingResponseDto> result = waitingService.getReservationWaitingsByMember(mock(Member.class));

        assertThat(result).hasSize(3);
        assertThat(result.stream().map(ReservationWaitingResponseDto::getWaitNum)).containsExactly(1L, 3L, 3L);
    }
}
