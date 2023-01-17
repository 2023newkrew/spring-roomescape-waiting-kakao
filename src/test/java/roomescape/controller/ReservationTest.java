package roomescape.controller;

import io.restassured.RestAssured;
import io.restassured.http.Header;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import roomescape.SpringWebApplication;
import roomescape.dto.LoginControllerTokenPostBody;
import roomescape.dto.ReservationsControllerPostBody;
import roomescape.entity.Member;
import roomescape.entity.Theme;
import roomescape.repository.MemberRepository;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ThemeRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;
import java.util.UUID;

import static org.hamcrest.core.Is.is;


@DisplayName("웹 요청 / 응답 처리로 입출력 추가")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = {SpringWebApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles({"web"})
public class ReservationTest {
    @Value("${local.server.port}")
    private int port;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ThemeRepository themeRepository;

    @Autowired
    private MemberRepository memberRepository;

    private Theme targetTheme = null;
    private Member memberOwner = null;
    private String memberOwnerToken = null;
    private Member memberOther = null;
    private String memberOtherToken = null;


    @BeforeAll
    void setupTheme() {
        var rand = new Random();
        RestAssured.port = port;
        targetTheme = themeRepository.selectById(themeRepository.insert(
                UUID.randomUUID().toString().split("-")[0],
                UUID.randomUUID().toString(),
                rand.nextInt(0, 10000000)
        )).get();
        var memberOwnerUsername = UUID.randomUUID().toString().split("-")[0];
        var memberOwnerPassword = UUID.randomUUID().toString().split("-")[0];
        var memberOtherUsername = UUID.randomUUID().toString().split("-")[0];
        var memberOtherPassword = UUID.randomUUID().toString().split("-")[0];
        var memberOwnerId = memberRepository.insert(memberOwnerUsername, memberOwnerPassword, UUID.randomUUID()
                                                                                                  .toString()
                                                                                                  .split("-")[0], "010-1234-5678");
        var memberOtherId = memberRepository.insert(memberOtherUsername, memberOtherPassword, UUID.randomUUID()
                                                                                                  .toString()
                                                                                                  .split("-")[0], "010-1234-5678");
        memberOwner = memberRepository.selectById(memberOwnerId);
        memberOther = memberRepository.selectById(memberOtherId);
        memberOwnerToken = RestAssured.given()
                                      .contentType(MediaType.APPLICATION_JSON_VALUE)
                                      .body(new LoginControllerTokenPostBody(memberOwnerUsername, memberOwnerPassword))
                                      .post("/login/token")
                                      .body().jsonPath().getString("access_token");
        memberOtherToken = RestAssured.given()
                                      .contentType(MediaType.APPLICATION_JSON_VALUE)
                                      .body(new LoginControllerTokenPostBody(memberOwnerUsername, memberOwnerPassword))
                                      .post("/login/token")
                                      .body().jsonPath().getString("access_token");
    }

    @DisplayName("예약 하기")
    @Test
    void createReservation() {
        var rand = new Random();
        RestAssured
                .given()
                .header(new Header("Authorization", "Bearer " + memberOwnerToken))
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new ReservationsControllerPostBody(
                        LocalDate.of(rand.nextInt(2000, 2200), rand.nextInt(1, 12), rand.nextInt(1, 28)),
                        LocalTime.of(rand.nextInt(0, 24), rand.nextInt(0, 60), 0),
                        UUID.randomUUID().toString().split("-")[0],
                        targetTheme.getId()
                ))
                .when()
                .post("/reservations")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());
    }

    @DisplayName("예약 조회")
    @Test
    void showReservation() {
        var rand = new Random();
        var targetReservation = reservationRepository.selectById(reservationRepository.insert(
                UUID.randomUUID().toString().split("-")[0],
                LocalDate.of(rand.nextInt(2000, 2200), rand.nextInt(1, 12), rand.nextInt(1, 28)),
                LocalTime.of(rand.nextInt(0, 24), rand.nextInt(0, 60), 0),
                targetTheme.getId(),
                memberOwner.getId()
        ).get()).get();

        RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get(String.format("/reservations/%d", targetReservation.getId()))
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("name", is(targetReservation.getName()))
                .body("date", is(targetReservation.getDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))))
                .body("time", is(targetReservation.getTime().format(DateTimeFormatter.ofPattern("HH:mm"))))
                .body("theme_id", is(targetReservation.getTheme().getId().intValue()))
                .body("theme_name", is(targetReservation.getTheme().getName()))
                .body("theme_desc", is(targetReservation.getTheme().getDesc()))
                .body("theme_price", is(targetReservation.getTheme().getPrice()));
    }

    @DisplayName("예약 취소")
    @Test
    void deleteReservation() {
        var rand = new Random();
        var targetReservation = reservationRepository.selectById(reservationRepository.insert(
                UUID.randomUUID().toString().split("-")[0],
                LocalDate.of(rand.nextInt(2000, 2200), rand.nextInt(1, 12), rand.nextInt(1, 28)),
                LocalTime.of(rand.nextInt(0, 24), rand.nextInt(0, 60), 0),
                targetTheme.getId(),
                memberOwner.getId()
        ).get()).get();

        RestAssured.given()
                   .header(new Header("Authorization", "Bearer " + memberOwnerToken))
                   .accept(MediaType.APPLICATION_JSON_VALUE)
                   .when()
                   .delete(String.format("/reservations/%d", targetReservation.getId()))
                   .then().log().all()
                   .statusCode(HttpStatus.NO_CONTENT.value());
    }

    @DisplayName("content-type이 application/json이 아닌 경우 값을 받지 않는다.")
    @ParameterizedTest
    @ValueSource(strings = {
            MediaType.TEXT_PLAIN_VALUE,
            MediaType.TEXT_HTML_VALUE,
            MediaType.TEXT_XML_VALUE,
            MediaType.APPLICATION_XML_VALUE,
    })
    void notJson(String contentType) {
        RestAssured.given().log().all().contentType(contentType).body("").when()
                   .post("/reservations").then().log().all()
                   .statusCode(HttpStatus.UNSUPPORTED_MEDIA_TYPE.value());
    }

    @DisplayName("예약 생성) 예약 생성 시 날짜와 시간이 똑같은 예약이 이미 있는 경우 예약을 생성할 수 없다.")
    @Test
    void failToCreateReservationAlreadyExist() {
        var rand = new Random();
        var targetReservation = reservationRepository.selectById(reservationRepository.insert(
                UUID.randomUUID().toString().split("-")[0],
                LocalDate.of(rand.nextInt(2000, 2200), rand.nextInt(1, 12), rand.nextInt(1, 28)),
                LocalTime.of(rand.nextInt(0, 24), rand.nextInt(0, 60), 0),
                targetTheme.getId(),
                memberOwner.getId()
        ).get()).get();

        RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE).body(new ReservationsControllerPostBody(
                        targetReservation.getDate(),
                        targetReservation.getTime(),
                        UUID.randomUUID().toString().split("-")[0],
                        targetTheme.getId()
                ))
                .when().post("/reservations")
                .then().log().all()
                .statusCode(HttpStatus.CONFLICT.value());
    }

    @DisplayName("예약 조회) ID가 없는 경우 조회 불가")
    @Test
    void notExistID() {
        RestAssured.given()
                   .when().get("/reservations/-1")
                   .then().log().all()
                   .statusCode(HttpStatus.NOT_FOUND.value());
    }

    @DisplayName("예약 삭제) ID가 없는 경우 삭제 불가")
    @Test
    void deleteNotExistId() {
        RestAssured.given()
                   .when().delete("/reservations/-1")
                   .then().log().all()
                   .statusCode(HttpStatus.NOT_FOUND.value());
    }
}
