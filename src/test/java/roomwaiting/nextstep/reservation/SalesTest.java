package roomwaiting.nextstep.reservation;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestExecutionListeners;
import roomwaiting.AcceptanceTestExecutionListener;
import roomwaiting.auth.token.JwtTokenProvider;
import roomwaiting.nextstep.RoomEscapeApplication;
import roomwaiting.nextstep.member.Member;
import roomwaiting.nextstep.reservation.domain.Reservation;
import roomwaiting.nextstep.theme.Theme;
import roomwaiting.nextstep.theme.ThemeDao;

import java.util.List;

import static roomwaiting.nextstep.reservation.ReservationStatus.*;

@SpringBootTest(classes = {RoomEscapeApplication.class})
@TestExecutionListeners(value = {AcceptanceTestExecutionListener.class,}, mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
public class SalesTest extends ReservationCommon {

    @Autowired
    JdbcTemplate jdbcTemplate;
    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    ThemeDao themeDao;

    private String memberToken;
    private String location;
    @BeforeEach
    public void setUp(){
        super.setUp();
        Member member = saveMember(jdbcTemplate, "member", "PASS1", "MEMBER");
        memberToken = jwtTokenProvider.createToken(String.valueOf(member.getId()), member.getRole());
        ExtractableResponse<Response> response = requestCreateReservation(memberToken);
        location = response.header("Location").split("/")[2];
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
