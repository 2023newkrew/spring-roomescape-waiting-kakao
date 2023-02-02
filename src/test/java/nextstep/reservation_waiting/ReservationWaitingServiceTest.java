package nextstep.reservation_waiting;

import auth.AuthenticationException;
import java.util.Optional;
import nextstep.exceptions.exception.NotFoundObjectException;
import nextstep.member.Member;
import nextstep.reservation.Reservation;
import nextstep.reservation.ReservationDao;
import nextstep.schedule.Schedule;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class ReservationWaitingServiceTest {

    @Mock
    private ReservationDao reservationDao;

    @InjectMocks
    private ReservationWaitingService reservationWaitingService;

    private Member member = Member.builder()
            .id(1L)
            .username("username")
            .password("password")
            .name("name")
            .phone("010-1234-5678")
            .role("member")
            .build();

    private Reservation reservation = Reservation.builder()
            .id(1L)
            .schedule(Schedule.builder()
                    .build())
            .createdDateTime(LocalDateTime.now())
            .member(member)
            .build();


    @DisplayName("존재하지 않는 예약 대기를 삭제하면 예외가 발생한다")
    @Test
    void delete() {
        given(reservationDao.findById(reservation.getId())).willReturn(Optional.empty());
        assertThatThrownBy(() -> reservationWaitingService.deleteById(member, reservation.getId()))
                .isInstanceOf(NotFoundObjectException.class);
    }

    @DisplayName("다른 사람의 예약 대기를 취소하면 예외가 발생한다")
    @Test
    void delete2() {
        Member anotherMember = Member.builder().id(100L).build();
        given(reservationDao.findById(reservation.getId())).willReturn(Optional.of(reservation));
        assertThatThrownBy(() -> reservationWaitingService.deleteById(anotherMember, reservation.getId()))
                .isInstanceOf(AuthenticationException.class);
    }

}
