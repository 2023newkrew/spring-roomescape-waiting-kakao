package nextstep.reservation_waiting;

import nextstep.member.Member;
import nextstep.reservation.Reservation;
import nextstep.schedule.Schedule;
import nextstep.support.exception.DuplicateEntityException;
import nextstep.support.exception.NotOwnReservationWaitingException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReservationWaitingServiceTest {
    @Mock
    private ReservationWaitingDao reservationWaitingDao;
    @InjectMocks
    private ReservationWaitingService reservationWaitingService;

    @Test
    @DisplayName("예약 대기 생성 테스트")
    void createTest() {
        Member member = Member.builder()
                .build();
        Reservation reservation = Reservation.builder()
                .build();

        when(reservationWaitingDao.save(any(Reservation.class), any(Member.class))).thenReturn(1L);
        assertThat(reservationWaitingService.create(reservation, member)).isEqualTo(1L);
    }

    @Test
    @DisplayName("자신의 대기 내역이 있으면 DUPLICATED 예외 발생 테스트")
    void createDuplicateTest() {
        Member member = Member.builder()
                .id(1L)
                .build();
        Reservation reservation = Reservation.builder()
                .build();
        ReservationWaiting reservationWaiting = ReservationWaiting.builder()
                .build();
        when(reservationWaitingDao.findByMemberId(anyLong())).thenReturn(Optional.of(reservationWaiting));
        assertThatThrownBy(() -> reservationWaitingService.create(reservation, member)).isInstanceOf(DuplicateEntityException.class);
    }

    @Test
    @DisplayName("자신의 예약 대기 목록 조회 테스트")
    void showOwnTest() {
        Schedule schedule = Schedule.builder()
                .id(1L)
                .build();
        List<ReservationWaiting> reservationWaitings = List.of(ReservationWaiting.builder()
                .id(1L)
                .schedule(schedule)
                .build(), ReservationWaiting.builder()
                .id(2L)
                .build());
        Member member = Member.builder()
                .id(1L)
                .build();
        when(reservationWaitingDao.findAllByMemberId(anyLong())).thenReturn(reservationWaitings);
        assertThat(reservationWaitingService.findOwn(member)).hasSize(2);
    }

    @Test
    @DisplayName("대기 번호 계산 기능 테스트")
    void calculateWaitingNumberTest() {
        Schedule schedule = Schedule.builder()
                .id(1L)
                .build();
        List<ReservationWaiting> reservationWaitings = List.of(ReservationWaiting.builder()
                .id(1L)
                .build(), ReservationWaiting.builder()
                .id(2L)
                .build(), ReservationWaiting.builder()
                .id(3L)
                .schedule(schedule)
                .build(), ReservationWaiting.builder()
                .id(4L)
                .build());
        ReservationWaiting reservationWaiting = reservationWaitings.get(2);
        when(reservationWaitingDao.findAllByScheduleId(anyLong())).thenReturn(reservationWaitings);
        assertThat(reservationWaitingService.calculateWaitNumber(reservationWaiting)).isEqualTo(2L);
    }

    @Test
    @DisplayName("자신의 예약대기를 취소한다.")
    void deleteTest() {
        ReservationWaiting reservationWaiting = ReservationWaiting.builder()
                .build();
        when(reservationWaitingDao.findById(anyLong())).thenReturn(Optional.ofNullable(reservationWaiting));
        assertThatCode(() -> reservationWaitingService.delete(1L)).doesNotThrowAnyException();
    }

    @Test
    @DisplayName("자신의 예약대기가 아닌 경우 NotOwnReservationWaitingException이 발생힌다")
    void validateOwnerTest() {
        Member owner = Member.builder()
                .id(1L)
                .build();
        Member other = Member.builder()
                .id(2L)
                .build();
        ReservationWaiting reservationWaiting = ReservationWaiting.builder()
                .member(owner)
                .build();
        assertThatThrownBy(() -> reservationWaitingService.validateByMember(reservationWaiting, other)).isInstanceOf(NotOwnReservationWaitingException.class);
    }
}
