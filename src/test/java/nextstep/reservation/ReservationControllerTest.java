package nextstep.reservation;

import auth.login.LoginService;
import auth.UserDetails;
import io.restassured.RestAssured;
import nextstep.member.Member;
import nextstep.member.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ReservationControllerTest {
    @LocalServerPort
    int port;

    @MockBean
    ReservationService reservationService;
    @MockBean
    MemberService memberService;
    @MockBean
    LoginService loginService;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    @DisplayName("자신의 예약 내역 조회")
    void readOwnReservationsTest() {
        List<Reservation> reservations = List.of(Reservation.builder()
                .id(1L)
                .build(), Reservation.builder()
                .id(2L)
                .build());
        Member member = Member.builder()
                .id(1L)
                .build();

        when(loginService.extractMember(anyString())).thenReturn(UserDetails.builder()
                .id(1L)
                .build());
        when(memberService.findById(anyLong())).thenReturn(member);
        when(reservationService.findOwn(any(Member.class))).thenReturn(reservations);

        RestAssured
                .given()
                .log()
                .all()
                .auth()
                .oauth2("token")
                .when()
                .get("/reservations/mine")
                .then()
                .log()
                .all()
                .statusCode(HttpStatus.OK.value())
                .body("size()", is(2));
    }
}
