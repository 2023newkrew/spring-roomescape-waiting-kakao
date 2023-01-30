package nextstep.reservation;

import nextstep.auth.AuthTestUtil;
import nextstep.auth.model.TokenResponse;
import nextstep.member.model.Member;
import nextstep.member.MemberTestUtil;
import nextstep.reservation.model.Reservation;
import nextstep.reservation.model.ReservationRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class ReservationE2ETest {

    @DisplayName("인증된 사용자는 예약할 수 있다.")
    @Test
    void test1() {
        Member reservationExistMember = MemberTestUtil.getReservationExistMember(1L);
        TokenResponse tokenResponse = AuthTestUtil.tokenLogin(reservationExistMember);
        ReservationRequest reservationRequest = new ReservationRequest(6L);

        ReservationTestUtil.createReservation(reservationRequest, tokenResponse.getAccessToken())
                .statusCode(HttpStatus.CREATED.value());
    }

    @DisplayName("인증되지 않은 사용자는 예약을 할 수 없다")
    @Test
    void test2() {
        ReservationRequest reservationRequest = new ReservationRequest(3L);

        ReservationTestUtil.createReservation(reservationRequest, "")
                .statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    @DisplayName("인증되지 않은 사용자는 예약을 조회할 수 없다.")
    @Test
    void test3() {
        ReservationTestUtil.requestReservationsAndGetValidatableResponse(1L, "2022-11-11", "")
                .statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    @DisplayName("본인의 예약만 조회할 수 있다.")
    @Test
    void test4() {
        Member reservationExistMember = MemberTestUtil.getReservationExistMember(1L);
        TokenResponse tokenResponse = AuthTestUtil.tokenLogin(reservationExistMember);

        List<Reservation> reservations = ReservationTestUtil.getReservations(1L, "2022-11-11", tokenResponse.getAccessToken());
        List<Reservation> notMyReservations = reservations.stream()
                .filter((reservation -> !reservation.getMemberName().equals(reservationExistMember.getMemberName())))
                .collect(Collectors.toList());

        assertThat(reservations.size()).isEqualTo(3);
        assertThat(notMyReservations.size()).isZero();
    }

    @DisplayName("인증된 사용자는 본인의 예약을 삭제할 수 있다.")
    @Test
    void test6() {
        Member reservationExistMember = MemberTestUtil.getReservationExistMember(1L);
        TokenResponse tokenResponse = AuthTestUtil.tokenLogin(reservationExistMember);

        ReservationTestUtil.removeReservation(1L, tokenResponse.getAccessToken())
                .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("인증되지 않은 사용자는 예약을 삭제할 수 없다.")
    @Test
    void test7() {
        ReservationTestUtil.removeReservation(1L, "")
                .statusCode(HttpStatus.UNAUTHORIZED.value());
    }

    @DisplayName("타인의 예약을 삭제할 수 없다.")
    @Test
    void test8() {
        Member reservationExistMember = MemberTestUtil.getReservationExistMember(1L);
        TokenResponse tokenResponse = AuthTestUtil.tokenLogin(reservationExistMember);

        ReservationTestUtil.removeReservation(5L, tokenResponse.getAccessToken())
                .statusCode(HttpStatus.UNAUTHORIZED.value());
    }
}
