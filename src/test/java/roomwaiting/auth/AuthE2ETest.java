package roomwaiting.auth;

import roomwaiting.auth.token.JwtProperties;
import roomwaiting.auth.token.JwtTokenProvider;
import roomwaiting.auth.token.TokenRequest;
import roomwaiting.auth.token.TokenResponse;
import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import roomwaiting.nextstep.RoomEscapeApplication;
import roomwaiting.nextstep.member.Member;
import roomwaiting.nextstep.member.MemberDao;
import roomwaiting.nextstep.member.MemberRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestExecutionListeners;
import roomwaiting.AcceptanceTestExecutionListener;

import static org.assertj.core.api.Assertions.assertThat;
import static roomwaiting.nextstep.AbstractE2ETest.*;
import static roomwaiting.support.Messages.ID;
import static roomwaiting.support.Messages.MEMBER_NOT_FOUND;

@SpringBootTest(classes = {RoomEscapeApplication.class})
@EnableConfigurationProperties(JwtProperties.class)
@TestExecutionListeners(value = {AcceptanceTestExecutionListener.class,}, mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
public class AuthE2ETest {
    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    MemberDao memberDao;

    @BeforeEach
    void setUp() {
        MemberRequest body = new MemberRequest(USERNAME, PASSWORD, "name", "010-1234-5678", "ADMIN");
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(body)
                .when().post("/members")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());
    }

    @DisplayName("토큰을 생성한다")
    @Test
    public void create() {
        TokenRequest body = new TokenRequest(USERNAME, PASSWORD);
        var response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(body)
                .when().post("/login/token")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();

        assertThat(response.as(TokenResponse.class)).isNotNull();
    }

    @Test
    @DisplayName("토큰을 이용하여 유저 정보를 가져올 수 있다.")
    void findByUsernameTest() {
        ExtractableResponse<Response> response = generateToken(USERNAME, PASSWORD);
        String accessToken = response.body().jsonPath().getString("accessToken");
        Long id = Long.valueOf(jwtTokenProvider.getPrincipal(accessToken));
        Member member = memberDao.findById(id).orElseThrow(() ->
                new NullPointerException(MEMBER_NOT_FOUND.getMessage() + ID + id)
        );
        Assertions.assertThat(member.getUsername()).isEqualTo(USERNAME);

        RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2(accessToken)
                .when().get("/members/me")
                .then().log().all()
                .statusCode(HttpStatus.OK.value());
    }
}
