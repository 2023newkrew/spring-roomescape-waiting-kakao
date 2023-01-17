package roomescape.controller;

import io.restassured.RestAssured;
import io.restassured.http.Header;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import roomescape.SpringWebApplication;
import roomescape.dto.LoginControllerTokenPostBody;
import roomescape.dto.MemberControllerPostBody;
import roomescape.repository.MemberRepository;
import roomescape.service.JWTProvider;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.is;

@DisplayName("[로그인/유저]웹 요청 / 응답 처리로 입출력 추가")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = {SpringWebApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles({"web"})
public class MemberLoginTest {
    @Autowired
    private MemberRepository repository;
    @Autowired
    private JWTProvider jwtProvider;

    @Value("${local.server.port}")
    private int port;

    @BeforeAll
    void setup() {
        RestAssured.port = port;
    }

    @DisplayName("회원 가입")
    @Test
    void createMember() {
        var body = new MemberControllerPostBody(
                UUID.randomUUID().toString().split("-")[0],
                UUID.randomUUID().toString().split("-")[0],
                UUID.randomUUID().toString().split("-")[0],
                UUID.randomUUID().toString().split("-")[0]
        );
        var res = RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(body)
                .when()
                .post("/members")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract();
        var member = repository.selectById(res.jsonPath().getLong("id"));
        assertThat(member.getName()).isEqualTo(body.getName());
        assertThat(member.getPhone()).isEqualTo(body.getPhone());
        assertThat(member.getUsername()).isEqualTo(body.getUsername());
        assertThat(member.getPassword()).isEqualTo(body.getPassword());
    }

    @DisplayName("로그인 토큰 확인")
    @Test
    void login() {
        var body = new MemberControllerPostBody(
                UUID.randomUUID().toString().split("-")[0],
                UUID.randomUUID().toString().split("-")[0],
                UUID.randomUUID().toString().split("-")[0],
                UUID.randomUUID().toString().split("-")[0]
        );
        var member = RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(body)
                .post("/members");
        var res = RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new LoginControllerTokenPostBody(body.getUsername(), body.getPassword()))
                .when().post("/login/token")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract();
        assertThat(jwtProvider.getSubject(res.body().jsonPath().getString("access_token")))
                .isEqualTo(Long.toString(member.getBody().jsonPath().getLong("id")));
    }

    @DisplayName("자기 자신의 정보 확인")
    @Test
    void me() {
        var body = new MemberControllerPostBody(
                UUID.randomUUID().toString().split("-")[0],
                UUID.randomUUID().toString().split("-")[0],
                UUID.randomUUID().toString().split("-")[0],
                UUID.randomUUID().toString().split("-")[0]
        );
        var memberId = RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(body)
                .post("/members")
                .body().jsonPath().getLong("id");
        var token = RestAssured
                .given()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new LoginControllerTokenPostBody(body.getUsername(), body.getPassword()))
                .when().post("/login/token")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value())
                .extract()
                .body().jsonPath().getString("access_token");

        RestAssured
                .given()
                .header(new Header("Authorization", "Bearer " + token))
                .when().get("/members/me")
                .then()
                .statusCode(HttpStatus.OK.value())
                .body("id", is(((int) memberId)))
                .body("name", is(body.getName()))
                .body("phone", is(body.getPhone()))
                .body("username", is(body.getUsername()))
                .body("password", is(body.getPassword()))
        ;
    }

    @DisplayName("토큰 없이 읽기 시도")
    @Test
    void invalidMeCall() {
        RestAssured
                .given()
                .header(new Header("Authorization", "Bearer " + ""))
                .when().get("/members/me")
                .then()
                .statusCode(HttpStatus.UNAUTHORIZED.value())
        ;
    }
}
