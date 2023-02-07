package nextstep.reservation;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import nextstep.exceptions.exception.ReservationStatusException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

public class ReservationStatusTest {

    @Test
    void 예약_미승인에서_예약_승인으로_변환할_수_있다() {
        assertThat(ReservationStatus.UN_APPROVE.transitStatus(ReservationStatus.APPROVE, "admin"))
                .isEqualTo(ReservationStatus.APPROVE);
    }

    @Test
    void 예약_승인은_관리자가_아니면_예외가_발생한다() {
        assertThatThrownBy(() -> ReservationStatus.UN_APPROVE.transitStatus(ReservationStatus.APPROVE, "member"))
                .isInstanceOf(ReservationStatusException.class);
    }

    @Test
    void 취소_대기_상태에서_취소_상태로_변환할_수_있다() {
        assertThat(ReservationStatus.CANCEL_WAIT.transitStatus(ReservationStatus.CANCEL, "admin"))
                .isEqualTo(ReservationStatus.CANCEL);
    }

    @Test
    void 취소는_관리자가_아니면_예외가_발생한다() {
        assertThatThrownBy(() -> ReservationStatus.CANCEL_WAIT.transitStatus(ReservationStatus.CANCEL, "member"))
                .isInstanceOf(ReservationStatusException.class);
    }

    @ParameterizedTest
    @EnumSource(value = ReservationStatus.class, names = {"UN_APPROVE", "APPROVE"})
    void 예약_미승인과_승인_상태에서_거절할_수_있다(ReservationStatus status) {
        assertThat(status.transitStatus(ReservationStatus.REJECT, "admin"))
                .isEqualTo(ReservationStatus.REJECT);
    }

    @Test
    void 예약_미승인_상테에서_취소할_수_있다() {
        assertThat(ReservationStatus.UN_APPROVE.transitStatus(ReservationStatus.CANCEL, "member"))
                .isEqualTo(ReservationStatus.CANCEL);
    }


    @Test
    void 예약_승인_상태에서_취소_대기로_변환할_수_있다() {
        assertThat(ReservationStatus.APPROVE.transitStatus(ReservationStatus.CANCEL_WAIT, "member"))
                .isEqualTo(ReservationStatus.CANCEL_WAIT);
    }

}
