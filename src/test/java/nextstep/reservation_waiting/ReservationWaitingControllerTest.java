package nextstep.reservation_waiting;

import auth.LoginService;
import auth.UserDetails;
import io.restassured.RestAssured;
import nextstep.member.Member;
import nextstep.member.MemberService;
import nextstep.reservation.Reservation;
import nextstep.reservation.ReservationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ReservationWaitingControllerTest {
    @LocalServerPort
    int port;

    @MockBean
    private MemberService memberService;
    @MockBean
    private ReservationService reservationService;
    @MockBean
    private ReservationWaitingService reservationWaitingService;
    @MockBean
    private LoginService loginService;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

    @Test
    @DisplayName("예약 대기 성공 테스트")
    void createTest() {
        ReservationWaitingRequest reservationWaitingRequest = new ReservationWaitingRequest(1L);
        Reservation reservation = Reservation.builder()
                .id(1L)
                .build();
        Member member = Member.builder()
                .id(1L)
                .build();

        when(loginService.extractMember(anyString())).thenReturn(UserDetails.builder()
                .id(1L)
                .build());
        when(memberService.findById(anyLong())).thenReturn(member);
        when(reservationService.findByScheduleId(anyLong())).thenReturn(Optional.ofNullable(reservation));
        when(reservationWaitingService.create(any(Reservation.class), any(Member.class))).thenReturn(1L);
        RestAssured
                .given()
                .log()
                .all()
                .auth()
                .oauth2("token")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(reservationWaitingRequest)
                .when()
                .post("/reservation-waitings")
                .then()
                .log()
                .all()
                .statusCode(HttpStatus.CREATED.value())
                .header("Location", "/reservation-waitings/1");
    }

    @Test
    @DisplayName("자신의 예약 대기 목록 조회 테스트")
    void readOwnReservationWaitingsTest(){
        List<ReservationWaiting> reservationWaitings = List.of(ReservationWaiting.builder()
                .id(1L)
                .build(), ReservationWaiting.builder()
                .id(2L)
                .build());
        Member member = Member.builder()
                .id(1L)
                .build();

        when(loginService.extractMember(anyString())).thenReturn(UserDetails.builder()
                .id(1L)
                .build());
        when(memberService.findById(anyLong())).thenReturn(member);
        when(reservationWaitingService.findOwn(any(Member.class))).thenReturn(reservationWaitings);

        RestAssured
                .given()
                .log()
                .all()
                .auth()
                .oauth2("token")
                .when()
                .get("/reservation-waitings/mine")
                .then()
                .log()
                .all()
                .statusCode(HttpStatus.OK.value())
                .body("size()", is(2));

    }

    @Test
    @DisplayName("에약 대기 삭제 테스트")
    void deleteTest() {
        when(loginService.extractMember(anyString())).thenReturn(UserDetails.builder()
                .id(1L)
                .build());

        RestAssured
                .given()
                .log()
                .all()
                .auth()
                .oauth2("token")
                .when()
                .delete("/reservation-waitings/1")
                .then()
                .log()
                .all()
                .statusCode(HttpStatus.NO_CONTENT.value());
    }
}
