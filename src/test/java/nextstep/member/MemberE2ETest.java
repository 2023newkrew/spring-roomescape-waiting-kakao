package nextstep.member;

import io.restassured.RestAssured;
import auth.domain.dto.TokenRequest;
import auth.domain.dto.TokenResponse;
import nextstep.AbstractE2ETest;
import nextstep.DatabaseCleaner;
import nextstep.domain.dto.request.MemberRequest;
import nextstep.domain.persist.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;

import javax.xml.crypto.Data;

import static org.assertj.core.api.Assertions.assertThat;

public class MemberE2ETest extends AbstractE2ETest {

    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";

    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @DisplayName("멤버를 생성한다")
    @Test
    public void create() {
        MemberRequest body = new MemberRequest("username", "password", "name", "010-1234-5678", "ADMIN");
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(body)
                .when().post("/members")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());
    }

    @DisplayName("내 정보를 조회한다")
    @Test
    public void showThemes() {
        var response = RestAssured
                .given().log().all()
                .auth().oauth2(token.getAccessToken())
                .when().get("/members/me")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();

        Member member = response.as(Member.class);
        assertThat(member.getUsername()).isNotNull();
    }
}
