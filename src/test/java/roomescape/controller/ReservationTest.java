package roomescape.controller;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import roomescape.SpringWebApplication;
import roomescape.dto.ReservationsControllerPostBody;
import roomescape.entity.Theme;
import roomescape.repository.ReservationRepository;
import roomescape.repository.ThemeRepository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.random.RandomGenerator;

import static org.hamcrest.core.Is.is;


@DisplayName("웹 요청 / 응답 처리로 입출력 추가")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = {SpringWebApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReservationTest {
    @Value("${local.server.port}")
    private int port;

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private ThemeRepository themeRepository;

    private Theme targetTheme = null;

    @BeforeAll
    void setupTheme() {
        var rand = RandomGenerator.getDefault();
        RestAssured.port = port;
        targetTheme = themeRepository.selectById(themeRepository.insert(
                UUID.randomUUID().toString().split("-")[0],
                UUID.randomUUID().toString(),
                rand.nextInt(0, 10000000)
        )).get();
    }

    @DisplayName("예약 하기")
    @Test
    void createReservation() {
        var rand = RandomGenerator.getDefault();
        RestAssured
                .given()
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
        var rand = RandomGenerator.getDefault();
        var targetReservation = reservationRepository.selectById(reservationRepository.insert(
                UUID.randomUUID().toString().split("-")[0],
                LocalDate.of(rand.nextInt(2000, 2200), rand.nextInt(1, 12), rand.nextInt(1, 28)),
                LocalTime.of(rand.nextInt(0, 24), rand.nextInt(0, 60), 0),
                targetTheme.getId()
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
        var rand = RandomGenerator.getDefault();
        var targetReservation = reservationRepository.selectById(reservationRepository.insert(
                UUID.randomUUID().toString().split("-")[0],
                LocalDate.of(rand.nextInt(2000, 2200), rand.nextInt(1, 12), rand.nextInt(1, 28)),
                LocalTime.of(rand.nextInt(0, 24), rand.nextInt(0, 60), 0),
                targetTheme.getId()
        ).get()).get();

        RestAssured.given()
                   .accept(MediaType.APPLICATION_JSON_VALUE)
                   .when()
                   .delete(String.format("/reservations/%d", targetReservation.getId()))
                   .then().log().all()
                   .statusCode(HttpStatus.NO_CONTENT.value());
    }
}
