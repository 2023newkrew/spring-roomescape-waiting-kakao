package nextstep.waiting;

import auth.token.JwtTokenProvider;
import io.restassured.RestAssured;
import nextstep.AbstractTest;
import nextstep.error.ErrorCode;
import nextstep.member.Member;
import nextstep.role.Role;
import nextstep.schedule.Schedule;
import nextstep.theme.Theme;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;

class ReservationWaitingE2ETest extends AbstractTest {

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    Theme theme;
    Schedule schedule;

    @BeforeEach
    void setUp() {
        theme = createNewTheme();
        schedule = createNewSchedule(theme.getId());
    }

    @Nested
    @DisplayName("예약 대기 생성 요청 api는")
    class CreateReservationWaiting {
        @Nested
        @DisplayName("예약 대기 생성 요청을 받은 스케줄에 예약이 존재하면")
        class hasReservation {
            String token;

            @BeforeEach
            void setUp() {
                Member member1 = createNewMember(Role.USER);
                createNewReservation(schedule.getId(), member1);

                Member member2 = createNewMember(Role.USER);
                token = jwtTokenProvider.createToken(String.valueOf(member2.getId()), member2.getUsername(), member2.getRole());
            }

            @Test
            @DisplayName("201 코드와 함께 /reservation-waitings/ 가 포함된 Location 헤더값을 응답으로 보낸다")
            void responseReservationWaiting() {
                ReservationWaitingRequest reservationWaitingRequest = new ReservationWaitingRequest(schedule.getId());

                RestAssured
                        .given().log().all()
                        .auth().oauth2(token)
                        .body(reservationWaitingRequest)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when().post("/reservation-waitings")
                        .then().log().all()
                        .statusCode(HttpStatus.CREATED.value())
                        .header(HttpHeaders.LOCATION, containsString("/reservation-waitings/"));
            }
        }

        @Nested
        @DisplayName("예약 대기 생성 요청을 받은 스케줄에 예약이 없으면")
        class noReservation {
            String token;

            @BeforeEach
            void setUp() {
                Member member = createNewMember(Role.USER);
                token = jwtTokenProvider.createToken(String.valueOf(member.getId()), member.getUsername(), member.getRole());
            }

            @Test
            @DisplayName("201 코드와 함께 /reservations/ 가 포함된 Location 헤더값을 응답으로 보낸다")
            void responseReservationWaiting() {
                ReservationWaitingRequest reservationWaitingRequest = new ReservationWaitingRequest(schedule.getId());

                RestAssured
                        .given().log().all()
                        .auth().oauth2(token)
                        .body(reservationWaitingRequest)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .when().post("/reservation-waitings")
                        .then().log().all()
                        .statusCode(HttpStatus.CREATED.value())
                        .header(HttpHeaders.LOCATION, containsString("/reservations/"));
            }
        }
    }

    @Nested
    @DisplayName("멤버의 모든 예약 대기 조회 api는")
    class findAllReservationWaitingsOfMember {
        @Nested
        @DisplayName("멤버가 여러개의 예약 대기를 가지고 있으면")
        class hasReservationWaitings {
            String token;

            ReservationWaiting reservationWaiting1;
            ReservationWaiting reservationWaiting2;
            ReservationWaiting reservationWaiting3;

            @BeforeEach
            void setUp() {
                Schedule schedule1 = createNewSchedule(theme.getId());
                Schedule schedule2 = createNewSchedule(theme.getId());

                Member member1 = createNewMember(Role.USER);
                createNewReservation(schedule.getId(), member1);
                createNewReservation(schedule1.getId(), member1);
                createNewReservation(schedule2.getId(), member1);

                Member member2 = createNewMember(Role.USER);
                reservationWaiting1 = createNewReservationWaiting(schedule.getId(), member2);
                reservationWaiting2 = createNewReservationWaiting(schedule1.getId(), member2);
                reservationWaiting3 = createNewReservationWaiting(schedule2.getId(), member2);

                token = jwtTokenProvider.createToken(String.valueOf(member2.getId()), member2.getUsername(), member2.getRole());
            }

            @Test
            @DisplayName("200 코드와 함께 메시지 바디에 멤버가 가진 예약 대기 리스트를 담아서 응답한다")
            void responseReseravtionWaitingLiss() {
                var response = RestAssured
                        .given().log().all()
                        .auth().oauth2(token)
                        .when().get("/reservation-waitings/mine")
                        .then().log().all()
                        .statusCode(HttpStatus.OK.value())
                        .extract();

                List<ReservationWaiting> reservations = response.jsonPath().getList(".", ReservationWaiting.class);
                assertThat(reservations)
                        .hasSize(3)
                        .contains(reservationWaiting1, reservationWaiting2, reservationWaiting3);
            }
        }
    }

    @Nested
    @DisplayName("예약 대기 삭제 api는")
    class DeleteReservationWaiting {
        @Nested
        @DisplayName("예약 대기 id에 해당하는 예약 대기가 존재하고, 해당 예약 대기의 소유자가 토큰에 해당하는 멤버이면")
        class ExistReservationWaitingAndIsOwner {
            String token;
            ReservationWaiting reservationWaiting;

            @BeforeEach
            void setUp() {
                Member member1 = createNewMember(Role.USER);
                createNewReservation(schedule.getId(), member1);

                Member member2 = createNewMember(Role.USER);
                reservationWaiting = createNewReservationWaiting(schedule.getId(), member2);

                token = jwtTokenProvider.createToken(String.valueOf(member2.getId()), member2.getUsername(), member2.getRole());
            }

            @Test
            @DisplayName("예약 대기를 삭제하고 204 코드로 응답한다")
            void responseNoContent() {
                RestAssured
                        .given().log().all()
                        .auth().oauth2(token)
                        .when().delete("/reservation-waitings/" + reservationWaiting.getReservationId())
                        .then().log().all()
                        .statusCode(HttpStatus.NO_CONTENT.value());
            }
        }

        @Nested
        @DisplayName("예약 대기 id에 해당하는 예약 대기가 존재하지만 해당 예약 대기의 소유자가 토큰에 해당하는 멤버가 아니면")
        class ExistReservationWaitingButIsNotOwner {
            String token;
            ReservationWaiting reservationWaiting;

            @BeforeEach
            void setUp() {
                Member member1 = createNewMember(Role.USER);
                createNewReservation(schedule.getId(), member1);

                Member member2 = createNewMember(Role.USER);
                reservationWaiting = createNewReservationWaiting(schedule.getId(), member2);

                Member member3 = createNewMember(Role.USER);
                token = jwtTokenProvider.createToken(String.valueOf(member3.getId()), member3.getUsername(), member3.getRole());
            }

            @Test
            @DisplayName("403코드와 함께 ErrorCode.FORBIDDEN 에 해당하는 코드와 설명을 바디에 담아서 응답한다")
            void responseForbidden() {
                RestAssured
                        .given().log().all()
                        .auth().oauth2(token)
                        .when().delete("/reservation-waitings/" + reservationWaiting.getReservationId())
                        .then().log().all()
                        .statusCode(HttpStatus.FORBIDDEN.value())
                        .body("code", is(ErrorCode.FORBIDDEN.getCode()))
                        .body("message", is(ErrorCode.FORBIDDEN.getDescription()));
            }
        }

        @Nested
        @DisplayName("삭제 요청을 받은 예약 대기가 존재하지 않으면")
        class NoReservationWaiting {
            String token;

            @BeforeEach
            void setUp() {
                Member member = createNewMember(Role.USER);
                token = jwtTokenProvider.createToken(String.valueOf(member.getId()), member.getUsername(), member.getRole());
            }

            @Test
            @DisplayName("404코드와 함께 ErrorCode.RESERVATION_NOT_FOUND 에 해당하는 코드와 설명을 바디에 담아서 응답한다")
            void responseNotFound() {
                RestAssured
                        .given().log().all()
                        .auth().oauth2(token)
                        .when().delete("/reservation-waitings/1")
                        .then().log().all()
                        .statusCode(HttpStatus.NOT_FOUND.value())
                        .body("code", is(ErrorCode.RESERVATION_NOT_FOUND.getCode()))
                        .body("message", is(ErrorCode.RESERVATION_NOT_FOUND.getDescription()));
            }
        }
    }
}
