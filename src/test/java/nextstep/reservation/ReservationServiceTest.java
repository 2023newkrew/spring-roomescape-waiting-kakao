package nextstep.reservation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

import java.time.LocalDateTime;
import java.util.Optional;
import nextstep.exceptions.exception.ReservationStatusException;
import nextstep.member.Member;
import nextstep.member.MemberDao;
import nextstep.schedule.Schedule;
import nextstep.schedule.ScheduleDao;
import nextstep.theme.ThemeDao;
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

    @Mock
    private ThemeDao themeDao;

    @Mock
    private ScheduleDao scheduleDao;

    @Mock
    private MemberDao memberDao;

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
                .role("member")
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
        assertThatThrownBy(() -> reservationService.cancelReservation(member, 1L))
                .isInstanceOf(NullPointerException.class);
    }


    @Test
    void 미승인_상태의_예약을_취소하면_예약은_취소가_된다() {
        reservation.setStatus(ReservationStatus.UN_APPROVE);
        given(reservationDao.findById(1L)).willReturn(Optional.of(reservation));
        assertThat(reservationService.cancelReservation(member, 1L).getStatus())
                .isEqualTo(ReservationStatus.CANCEL);
    }

    @Test
    void 승인_상태의_예약을_취소하면_예약은_취소대기가_된다() {
        reservation.setStatus(ReservationStatus.APPROVE);
        given(reservationDao.findById(1L)).willReturn(Optional.of(reservation));
        assertThat(reservationService.cancelReservation(member, 1L).getStatus())
                .isEqualTo(ReservationStatus.CANCEL_WAIT);
    }

    @ParameterizedTest
    @EnumSource(value = ReservationStatus.class, names = {"CANCEL", "CANCEL_WAIT", "REJECT"})
    void 승인_미승인_상태가_아닌_예약은_예외가_발생한다(ReservationStatus status) {
        reservation.setStatus(status);
        given(reservationDao.findById(1L)).willReturn(Optional.of(reservation));
        assertThatThrownBy(() -> reservationService.cancelReservation(member, 1L))
                .isInstanceOf(ReservationStatusException.class);
    }

    @Test
    void 취소대기_상태의_예약을_취소할_수_있다() {
        reservation.setStatus(ReservationStatus.CANCEL_WAIT);
        given(reservationDao.findById(reservation.getId())).willReturn(Optional.of(reservation));
        assertThat(reservationService.cancelReservationFromAdmin(reservation.getId()).getStatus())
                .isEqualTo(ReservationStatus.CANCEL);

    }

    @ParameterizedTest
    @EnumSource(value = ReservationStatus.class, names = {"UN_APPROVE", "APPROVE", "CANCEL", "REJECT"})
    void 취소대기_상태_이외의_예약은_취소하면_예외가_발생한다(ReservationStatus status) {
        reservation.setStatus(status);
        given(reservationDao.findById(reservation.getId())).willReturn(Optional.of(reservation));
        assertThatThrownBy(() -> reservationService.cancelReservationFromAdmin(reservation.getId()))
                .isInstanceOf(ReservationStatusException.class);
    }

    @Test
    void 예약_미승인_상태의_예약을_승인할_수_있다() {
        reservation.setStatus(ReservationStatus.UN_APPROVE);
        given(reservationDao.findById(reservation.getId())).willReturn(Optional.of(reservation));
        assertThat(reservationService.approveReservationFromAdmin(reservation.getId()).getStatus())
                .isEqualTo(ReservationStatus.APPROVE);
    }

    @ParameterizedTest
    @EnumSource(value = ReservationStatus.class, names = {"CANCEL_WAIT", "APPROVE", "CANCEL", "REJECT"})
    void 예약_미승인_이외의_예약은_승인하면_예외가_발생한다(ReservationStatus status) {
        reservation.setStatus(status);
        given(reservationDao.findById(reservation.getId())).willReturn(Optional.of(reservation));
        assertThatThrownBy(() -> reservationService.approveReservationFromAdmin(reservation.getId()))
                .isInstanceOf(ReservationStatusException.class);
    }

    @ParameterizedTest
    @EnumSource(value = ReservationStatus.class, names = {"UN_APPROVE", "APPROVE"})
    void 예약_거절을_할_수_있다(ReservationStatus status) {
        reservation.setStatus(status);
        given(reservationDao.findById(reservation.getId())).willReturn(Optional.of(reservation));
        assertThat(reservationService.rejectReservationFromAdmin(reservation.getId()).getStatus())
                .isEqualTo(ReservationStatus.REJECT);
    }

    @ParameterizedTest
    @EnumSource(value = ReservationStatus.class, names = {"CANCEL_WAIT", "CANCEL", "REJECT"})
    void 예약_미승인과_승인외의_예약을_거절하면_예외가_발생한다(ReservationStatus status) {
        reservation.setStatus(status);
        given(reservationDao.findById(reservation.getId())).willReturn(Optional.of(reservation));
        assertThatThrownBy(() -> reservationService.rejectReservationFromAdmin(reservation.getId()))
                .isInstanceOf(ReservationStatusException.class);
    }
}
