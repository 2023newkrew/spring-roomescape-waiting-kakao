package controller;

import auth.dto.TokenRequest;
import nextstep.member.dto.MemberRequest;
import nextstep.reservation.dto.ReservationRequest;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

public class ReservationControllerTest extends AbstractControllerTest {

    static final String DEFAULT_PATH = "/reservations";

    String anotherToken;

    @Override
    void setUpTemplate() {
        super.setUpTemplate();

        createMember();
    }

    private void createMember() {
        var request = new MemberRequest("member", "password", "member", "-");
        var tokenRequest = new TokenRequest(request.getUsername(), request.getPassword());
        post(given(), "/members", request);
        anotherToken = createProvider(tokenRequest).createToken(tokenRequest).getAccessToken();
    }

    @TestInstance(TestInstance.Lifecycle.PER_CLASS)
    @Nested
    class delete {
        @DisplayName("예약 취소 시 예약 대기가 예약으로 바뀌는지 확인")
        @Test
        void should_deleteWaiting_when_mine() {
            createReservation();
            createWaiting();
            delete(authGiven(), DEFAULT_PATH + "/1");

            var response = get(authGiven(anotherToken), DEFAULT_PATH + "/mine");

            then(response)
                    .body("member.id", Matchers.contains(2));
        }

        private void createReservation() {
            var request = new ReservationRequest(1L);
            post(authGiven(), "/reservations", request);
        }

        private void createWaiting() {
            var request = new ReservationRequest(1L);
            post(authGiven(anotherToken), "/reservation-waitings", request);
        }
    }
}
