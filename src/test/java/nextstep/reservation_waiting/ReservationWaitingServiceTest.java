package nextstep.reservation_waiting;

import nextstep.member.Member;
import nextstep.reservation.Reservation;
import nextstep.support.exception.DuplicateEntityException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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
}
