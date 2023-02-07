package nextstep.reservation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import java.time.LocalDateTime;
import java.util.Optional;
import nextstep.exceptions.exception.ReservationStatusException;
import nextstep.member.Member;
import nextstep.schedule.Schedule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ReservationServiceTest {

    @Mock
    private ReservationDao reservationDao;

    @InjectMocks
    private ReservationService reservationService;

    private Member member;
    private Reservation reservation;
    @BeforeEach
    void setUp() {
        member = Member.builder()
                .id(1L)
                .phone("010-1234-5678")
                .name("name")
                .role("admin")
                .build();
        reservation = Reservation.builder()
                .id(1L)
                .schedule(Schedule.builder().build())
                .member(member)
                .createdDateTime(LocalDateTime.now())
                .status(ReservationStatus.UN_APPROVE)
                .build();
    }


    @Test
    void 예약이_존재하지_않으면_예외를_발생시킨다() {
        assertThatThrownBy(() -> reservationService.updateReservationStatus(1L, member, ReservationStatus.CANCEL))
                .isInstanceOf(NullPointerException.class);
    }


    @Test
    void 미승인_상태의_예약을_취소하면_예약은_취소가_된다() {
        reservation.setStatus(ReservationStatus.UN_APPROVE);
        given(reservationDao.findById(1L)).willReturn(Optional.of(reservation));
        assertThat(reservationService.updateReservationStatus(1L, member, ReservationStatus.CANCEL).getStatus())
                .isEqualTo(ReservationStatus.CANCEL);
    }

    @Test
    void 승인_상태의_예약을_취소하면_예약은_취소대기가_된다() {
        reservation.setStatus(ReservationStatus.APPROVE);
        given(reservationDao.findById(1L)).willReturn(Optional.of(reservation));
        assertThat(reservationService.updateReservationStatus(1L,member, ReservationStatus.CANCEL_WAIT).getStatus())
                .isEqualTo(ReservationStatus.CANCEL_WAIT);
    }

    @Test
    void 취소대기_상태의_예약을_취소할_수_있다() {
        reservation.setStatus(ReservationStatus.CANCEL_WAIT);
        given(reservationDao.findById(reservation.getId())).willReturn(Optional.of(reservation));

        assertThat(reservationService.updateReservationStatus(reservation.getId(), member, ReservationStatus.CANCEL).getStatus())
                .isEqualTo(ReservationStatus.CANCEL);

    }

    @ParameterizedTest
    @EnumSource(value = ReservationStatus.class, names = {"APPROVE", "CANCEL", "REJECT"})
    void 취소대기와_미승인_상태_이외의_예약은_취소하면_예외가_발생한다(ReservationStatus status) {
        reservation.setStatus(status);
        given(reservationDao.findById(reservation.getId())).willReturn(Optional.of(reservation));
        assertThatThrownBy(() -> reservationService.updateReservationStatus(reservation.getId(), member, ReservationStatus.CANCEL))
                .isInstanceOf(ReservationStatusException.class);
    }

    @Test
    void 예약_미승인_상태의_예약을_승인할_수_있다() {
        reservation.setStatus(ReservationStatus.UN_APPROVE);
        given(reservationDao.findById(reservation.getId())).willReturn(Optional.of(reservation));
        assertThat(reservationService.updateReservationStatus(reservation.getId(), member, ReservationStatus.APPROVE).getStatus())
                .isEqualTo(ReservationStatus.APPROVE);
    }

    @ParameterizedTest
    @EnumSource(value = ReservationStatus.class, names = {"CANCEL_WAIT", "APPROVE", "CANCEL", "REJECT"})
    void 예약_미승인_이외의_예약은_승인하면_예외가_발생한다(ReservationStatus status) {
        reservation.setStatus(status);
        given(reservationDao.findById(reservation.getId())).willReturn(Optional.of(reservation));
        assertThatThrownBy(() -> reservationService.updateReservationStatus(reservation.getId(), member, ReservationStatus.APPROVE))
                .isInstanceOf(ReservationStatusException.class);
    }

    @ParameterizedTest
    @EnumSource(value = ReservationStatus.class, names = {"UN_APPROVE", "APPROVE"})
    void 예약_거절을_할_수_있다(ReservationStatus status) {
        reservation.setStatus(status);
        given(reservationDao.findById(reservation.getId())).willReturn(Optional.of(reservation));
        assertThat(reservationService.updateReservationStatus(reservation.getId(), member, ReservationStatus.REJECT).getStatus())
                .isEqualTo(ReservationStatus.REJECT);
    }

    @ParameterizedTest
    @EnumSource(value = ReservationStatus.class, names = {"CANCEL_WAIT", "CANCEL", "REJECT"})
    void 예약_미승인과_승인외의_예약을_거절하면_예외가_발생한다(ReservationStatus status) {
        reservation.setStatus(status);
        given(reservationDao.findById(reservation.getId())).willReturn(Optional.of(reservation));
        assertThatThrownBy(() -> reservationService.updateReservationStatus(reservation.getId(), member, ReservationStatus.REJECT))
                .isInstanceOf(ReservationStatusException.class);
    }
}
