package nextstep.reservation_waiting;

import nextstep.member.Member;
import nextstep.reservation.Reservation;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
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
}
