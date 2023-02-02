package roomwaiting.nextstep.member;

import io.restassured.RestAssured;
import org.junit.jupiter.api.Assertions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import roomwaiting.nextstep.RoomEscapeApplication;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestExecutionListeners;
import roomwaiting.AcceptanceTestExecutionListener;

import static roomwaiting.nextstep.AbstractE2ETest.*;

@SpringBootTest(classes = {RoomEscapeApplication.class})
@TestExecutionListeners(value = {AcceptanceTestExecutionListener.class,}, mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
public class MemberTest {
    @Autowired
    JdbcTemplate jdbcTemplate;

    @DisplayName("멤버를 생성할 수 있다")
    @Test
    void create() {
        MemberRequest body = new MemberRequest(USERNAME, PASSWORD, "name", "010-1234-5678", "MEMBER");
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(body)
                .when().post("/members")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());
    }

    @DisplayName("조회를 할 때 로그인이 되지 않았을 경우, 에러 발생")
    @Test
    void notLogin() {
        create();
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/members/me")
                .then().log().all()
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("잘못된 토큰을 입력하는 경우, 에러 발생")
    @Test
    void errorToken() {
        create();
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .auth().oauth2("ASDASD.asdasd'asdasd")
                .when().get("/members/me")
                .then().log().all()
                .statusCode(HttpStatus.FORBIDDEN.value());
    }

    @DisplayName("동일한 멤버가 중복 등록되는 경우, 에러 발생")
    @Test
    void duplicatedMemberTest(){
        saveMember(jdbcTemplate, USERNAME + "admin", PASSWORD, "ADMIN");
        Assertions.assertThrows(DuplicateKeyException.class, () ->
            saveMember(jdbcTemplate, USERNAME + "admin", PASSWORD, "ADMIN")
        );
    }

}
