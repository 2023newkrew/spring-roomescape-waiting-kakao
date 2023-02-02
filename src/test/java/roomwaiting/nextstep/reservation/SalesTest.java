package roomwaiting.nextstep.reservation;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import roomwaiting.auth.token.JwtTokenProvider;
import roomwaiting.nextstep.member.Member;
import roomwaiting.nextstep.reservation.domain.Reservation;
import roomwaiting.nextstep.reservation.dto.ReservationRequest;
import roomwaiting.nextstep.schedule.ScheduleRequest;
import roomwaiting.nextstep.theme.Theme;
import roomwaiting.nextstep.theme.ThemeDao;

import java.util.List;

import static roomwaiting.nextstep.reservation.ReservationStatus.*;


public class SalesTest extends ReservationCommon {

    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    ThemeDao themeDao;

    private Member member1;
    private String memberToken;
    private String location;
    @BeforeEach
    public void setUp(){
        super.setUp();
        member1 = saveMember(jdbcTemplate, "member1", "PASS1", "MEMBER");
        memberToken = jwtTokenProvider.createToken(String.valueOf(member1.getId()), member1.getRole());
        ExtractableResponse<Response> response = requestCreateReservation(request, memberToken);
        location = response.header("Location").split("/")[2]; // reservation Location
    }


    @DisplayName("예약 승인으로 변경되는 경우 매출이 올라간다")
    @Test
    void increaseSales(){
        String amountPre = requestGrossSales().body().asString();
        Assertions.assertThat(amountPre).isEqualTo("0");

        ExtractableResponse<Response> adminRequest = requestApprove(location, token.getAccessToken());
        Assertions.assertThat(adminRequest.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<Reservation> lookUpPost = lookUpReservation(token.getAccessToken()).jsonPath().getList(".", Reservation.class);
        Assertions.assertThat(lookUpPost.size()).isEqualTo(1);
        Assertions.assertThat(lookUpPost.get(0).getStatus()).isEqualTo(APPROVED);

        String amountPost = requestGrossSales().body().asString();
        Theme theme = themeDao.findById(themeId).get();
        Assertions.assertThat(amountPost).isNotEqualTo("0");
        Assertions.assertThat(amountPost).isEqualTo(String.valueOf(theme.getPrice()));
    }

    @DisplayName("관리자의 취소승인으로 인해 예약이 취소대기에서 취소로 변경되는 경우 매출이 감소한다")
    @Test
    void decreaseSales(){

        requestApprove(location, token.getAccessToken());
        Assertions.assertThat(requestGrossSales().body().asString()).isEqualTo("22000");

        cancelReservation(location, memberToken);
        List<Reservation> lookUpPre = lookUpReservation(memberToken).jsonPath().getList(".", Reservation.class);
        Assertions.assertThat(lookUpPre.get(0).getStatus()).isEqualTo(CANCEL_WAIT);
        requestCancelApprove(location, token.getAccessToken());

        List<Reservation> lookUpPost = lookUpReservation(memberToken).jsonPath().getList(".", Reservation.class);
        Assertions.assertThat(lookUpPost.get(0).getStatus()).isEqualTo(CANCEL);

        Assertions.assertThat(requestGrossSales().body().asString()).isEqualTo("0");
    }

    @DisplayName("예약 승인 상태에서 관리자가 취소하는 경우 매출이 감소한다")
    @Test
    void adminCancelSalesTest(){
        Assertions.assertThat(requestAllSales().jsonPath().getList(".").size()).isEqualTo(0);
        requestApprove(location, token.getAccessToken());
        Assertions.assertThat(requestGrossSales().body().asString()).isEqualTo("22000");
        Assertions.assertThat(requestAllSales().jsonPath().getList(".").size()).isEqualTo(1);
        cancelReservation(location, token.getAccessToken());
        Assertions.assertThat(requestGrossSales().body().asString()).isEqualTo("0");
        Assertions.assertThat(requestAllSales().jsonPath().getList(".").size()).isEqualTo(2);
    }

    @DisplayName("매출 변경 히스토리를 user별로 조회할 수 있다")
    @Test
    void salesLookUpMemberFilterTest(){
        requestApprove(location, token.getAccessToken());
        Assertions.assertThat(lookUpMemberId(member1.getId()).jsonPath().getList(".").size()).isEqualTo(1);

        ReservationRequest request1 = requestCreator("2023-02-01", "12:00:00");
        ExtractableResponse<Response> response1 = requestCreateReservation(request1, memberToken);
        String location1 = response1.header("Location").split("/")[2]; // reservation Location
        requestApprove(location1, token.getAccessToken());
        Assertions.assertThat(lookUpMemberId(member1.getId()).jsonPath().getList(".").size()).isEqualTo(2);

        Member member2 = saveMember(jdbcTemplate, "member2", "PASS1", "MEMBER");
        String memberToken2 = jwtTokenProvider.createToken(String.valueOf(member2.getId()), member2.getRole());
        ReservationRequest request2 = requestCreator("2023-02-02", "13:00:00");
        ExtractableResponse<Response> response2 = requestCreateReservation(request2, memberToken2);
        String location2 = response2.header("Location").split("/")[2]; // reservation Location
        requestApprove(location2, token.getAccessToken());
        Assertions.assertThat(lookUpMemberId(member1.getId()).jsonPath().getList(".").size()).isEqualTo(2);
        Assertions.assertThat(lookUpMemberId(member2.getId()).jsonPath().getList(".").size()).isEqualTo(1);

        Theme theme = themeDao.findById(themeId).get();
        Assertions.assertThat(requestGrossSales().body().asString()).isEqualTo(String.valueOf(3*theme.getPrice()));
    }

    private ReservationRequest requestCreator(String date, String time){
        ScheduleRequest scheduleRequest = new ScheduleRequest(themeId, date, time);
        var scheduleResponse = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(token.getAccessToken())
                .body(scheduleRequest)
                .when().post("/admin/schedules")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract();
        String[] scheduleLocation = scheduleResponse.header("Location").split("/");
        scheduleId = Long.parseLong(scheduleLocation[scheduleLocation.length - 1]);
        return new ReservationRequest(scheduleId);
    }

    private ExtractableResponse<Response> lookUpMemberId(Long memberId){
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(token.getAccessToken())
                .body(request)
                .when().get("/admin/sales/members/" + memberId)
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> requestGrossSales(){
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(token.getAccessToken())
                .body(request)
                .when().get("/admin/sales/gross")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> requestAllSales(){
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(token.getAccessToken())
                .body(request)
                .when().get("/admin/sales/")
                .then().log().all()
                .extract();
    }

}
