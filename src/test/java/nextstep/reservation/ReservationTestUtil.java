package nextstep.reservation;

import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import nextstep.reservation.model.Reservation;
import nextstep.reservation.model.ReservationRequest;
import org.springframework.http.MediaType;

import java.util.List;

public class ReservationTestUtil {

    public static final String RESERVATION_URL = "/reservations";

    public static ValidatableResponse createReservation(ReservationRequest reservationRequest, String accessToken) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .body(reservationRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post(RESERVATION_URL)
                .then().log().all();
    }

    public static ValidatableResponse removeReservation(Long id, String accessToken) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .when().delete(RESERVATION_URL + "/" + id)
                .then().log().all();
    }

    public static ValidatableResponse requestReservationsAndGetValidatableResponse(Long themeId, String date, String accessToken) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .param("themeId", themeId)
                .param("date", date)
                .when().get(RESERVATION_URL)
                .then().log().all();
    }

    public static  List<Reservation> getReservations(Long themeId, String date, String accessToken) {
        return requestReservationsAndGetValidatableResponse(themeId, date, accessToken)
                .extract().response()
                .jsonPath()
                .getList(".", Reservation.class);
    }

    public static  ValidatableResponse requestMyReservationsAndGetValidatableResponse(String accessToken) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .when().get(RESERVATION_URL + "/mine")
                .then().log().all();
    }

    public static  List<Reservation> getMyReservations(String accessToken) {
        return requestMyReservationsAndGetValidatableResponse(accessToken)
                .extract().response()
                .jsonPath()
                .getList(".", Reservation.class);

    }
}