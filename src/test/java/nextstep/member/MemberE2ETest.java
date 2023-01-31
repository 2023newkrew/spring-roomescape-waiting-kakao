package nextstep.member;

import io.restassured.RestAssured;
import nextstep.AbstractE2ETest;
import nextstep.domain.dto.request.MemberRequest;
import nextstep.domain.persist.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static org.assertj.core.api.Assertions.assertThat;

public class MemberE2ETest extends AbstractE2ETest {
    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @DisplayName("멤버를 생성한다")
    @Test
    public void Should_CreateMember_When_Request() {
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
    public void Should_GetMemberInfo_When_Request() {
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
