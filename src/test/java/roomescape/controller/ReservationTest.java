package roomescape.controller;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import roomescape.SpringWebApplication;
import roomescape.dto.ReservationsControllerPostBody;
import roomescape.entity.Reservation;
import roomescape.entity.Theme;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.hamcrest.core.Is.is;


@DisplayName("웹 요청 / 응답 처리로 입출력 추가")
@SpringBootTest(classes = {SpringWebApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ReservationTest {

    private static final String ADD_SQL = "INSERT INTO reservation (date, time, name, theme_name, theme_desc, theme_price) VALUES (?, ?, ?, ?, ?, ?);";

    private static final LocalDate DATE = LocalDate.parse("2022-08-01");
    private static final LocalTime TIME = LocalTime.parse("13:00");
    private static final String NAME = "test";
    private static final String THEME_NAME = "워너고홈";
    private static final String THEME_DESC = "병맛 어드벤처 회사 코믹물";
    private static final int THEME_PRICE = 29000;
    private static final Theme THEME = new Theme(null, "워너고홈", "병맛 어드벤처 회사 코믹물", 29000);

    @LocalServerPort
    int port;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;

        jdbcTemplate.execute("DROP TABLE RESERVATION IF EXISTS");
        jdbcTemplate.execute("CREATE TABLE RESERVATION("
                + "id bigint not null auto_increment, date date, time time,"
                + " name varchar(20), theme_name varchar(20), theme_desc  varchar(255),"
                + " theme_price int, primary key (id));");

        List<Object[]> split = List.<Object[]>of(
                new Object[]{DATE, TIME, NAME, THEME_NAME, THEME_DESC, THEME_PRICE});

        jdbcTemplate.batchUpdate(ADD_SQL, split);
    }

    @DisplayName("예약 하기")
    @Test
    void createReservation() {
        Reservation reservation = new Reservation(null, LocalDate.parse("2022-08-11"),
                LocalTime.parse("13:00:00"), "name", THEME);

        RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new ReservationsControllerPostBody(
                        LocalDate.parse("2022-08-11"),
                        LocalTime.parse("13:00:00"),
                        "name",
                        THEME_NAME,
                        THEME_DESC,
                        THEME_PRICE
                ))
                .when()
                .post("/reservations")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());
    }

    @DisplayName("예약 조회")
    @Test
    void showReservation() {
        RestAssured
                .given()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/reservations/1")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .body("name", is(NAME))
                .body("date", is(DATE.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))))
                .body("time", is(TIME.format(DateTimeFormatter.ofPattern("HH:mm"))));
    }

    @DisplayName("예약 취소")
    @Test
    void deleteReservation() {
        RestAssured.given()
                   .accept(MediaType.APPLICATION_JSON_VALUE)
                   .when()
                   .delete("/reservations/1")
                   .then().log().all()
                   .statusCode(HttpStatus.NO_CONTENT.value());
    }
}
