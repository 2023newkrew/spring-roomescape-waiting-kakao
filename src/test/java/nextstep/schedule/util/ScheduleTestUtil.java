package nextstep.schedule.util;

import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import nextstep.schedule.model.Schedule;
import nextstep.schedule.model.ScheduleRequest;
import org.springframework.http.MediaType;

import java.util.List;

public class ScheduleTestUtil {
    public static List<Schedule> getSchedules(Long themeId, String date) {
        return requestScheduleAndGetValidatableResponse(themeId, date)
                .extract()
                .jsonPath()
                .getList(".");
    }

    public static ValidatableResponse requestScheduleAndGetValidatableResponse(Long themeId, String date) {
        return RestAssured
                .given().log().all()
                .param("themeId", themeId)
                .param("date", date)
                .when().get("/schedules")
                .then().log().all();
    }

    public static ValidatableResponse deleteScheduleAndGetValidatableResponse(Long id, String accessToken) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .when().delete("/admin/schedules/" + id)
                .then().log().all();
    }

    public static ValidatableResponse createScheduleAndGetValidatableResponse(ScheduleRequest body, String accessToken) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(accessToken)
                .body(body)
                .when().post("/admin/schedules")
                .then().log().all();
    }
}