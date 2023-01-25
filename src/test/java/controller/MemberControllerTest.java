package e2e;

import io.restassured.RestAssured;
import nextstep.member.dto.MemberRequest;
import nextstep.member.dto.MemberResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class MemberE2ETest extends AbstractE2ETest {

    @DisplayName("멤버를 생성한다")
    @Test
    public void create() {
        var request = new MemberRequest(
                "username",
                "password",
                "name",
                "010-1234-5678"
        );

        var response = post("/members", request);

        then(response)
                .statusCode(HttpStatus.CREATED.value())
                .header("Location", "/members/1");
    }

    @DisplayName("ID가 같은 멤버를 찾는다")
    @Test
    public void getById() {
        MemberRequest request = new MemberRequest(
                "username",
                "password",
                "name",
                "010-1234-5678"
        );
        var locations = createMember(request);

        MemberResponse response = getMember(locations);

        Assertions.assertThat(response)
                .extracting(
                        MemberResponse::getUsername,
                        MemberResponse::getPassword,
                        MemberResponse::getName,
                        MemberResponse::getPhone
                )
                .contains(
                        request.getUsername(),
                        request.getPassword(),
                        request.getName(),
                        request.getPhone()
                );
    }

    private String createMember(MemberRequest request) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/members")
                .getHeader("Location");
    }

    private MemberResponse getMember(String locations) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get(locations)
                .as(MemberResponse.class);
    }
}
