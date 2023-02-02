package nextstep.reservation;

import nextstep.member.Member;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock
    public ReservationDao reservationDao;
    @InjectMocks
    private ReservationService reservationService;

    @Test
    @DisplayName("자신의 예약 내역을 조회한다.")
    void showOwnTest() {
        List<Reservation> reservations = List.of(Reservation.builder()
                .id(1L)
                .build(), Reservation.builder()
                .id(2L)
                .build());
        Member member = Member.builder()
                .id(1L)
                .build();

        when(reservationDao.findAllByMemberId(anyLong())).thenReturn(reservations);
        assertThat(reservationService.findOwn(member)).hasSize(2);
    }

}
