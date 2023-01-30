package nextstep.member;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import nextstep.member.model.Member;
import nextstep.member.model.MemberRequest;
import org.springframework.http.MediaType;

import java.util.List;

public class MemberTestUtil {
    public static final Member RESERVATION_EXIST_MEMBER_1 = new Member(1L, "reservation_exist_member1", "password", "name", "010-1234-5678");
    public static final Member RESERVATION_EXIST_MEMBER_2 = new Member(2L, "reservation_exist_member2", "password", "name", "010-1234-5678");
    public static final Member RESERVATION_NOT_EXIST_MEMBER = new Member("'no_reservation_exist_member'", "'password'", "name", "010-1234-5678");

    public static final Member NOT_EXIST_MEMBER = new Member("NOT_EXIST_MEMBER_NAME", "password", "name", "010-1234-5678");
    public static final Member ROLE_ADMIN_MEMBER = new Member("role_admin", "password", "name", "010-1234-5678");
    public static final Member ROLE_MEMBER_MEMBER = new Member("role_member", "password", "name", "010-1234-5678");

    public static final List<Member> RESERVATION_EXIST_MEMBER_LIST = List.of(RESERVATION_EXIST_MEMBER_1, RESERVATION_EXIST_MEMBER_2);

    public static Response createMember(MemberRequest memberRequest) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(memberRequest)
                .when().post("/members");
    }

    public static ValidatableResponse createMemberAndGetValidatableResponse(MemberRequest memberRequest) {
        return createMember(memberRequest)
                .then().log().all();
    }


    public static Member getMemberSelfInfo(String accessToken) {
        return getMemberSelfInfoAndGetValidatableResponse(accessToken)
                .extract().as(Member.class);
    }

    public static ValidatableResponse getMemberSelfInfoAndGetValidatableResponse(String accessToken) {
        return RestAssured
                .given().log().all()
                .auth().oauth2(accessToken)
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/members/me")
                .then().log().all();
    }

    public static Member getReservationExistMember(Long id){
        return RESERVATION_EXIST_MEMBER_LIST.stream()
                .filter((reservationExistMember) -> id.equals(reservationExistMember.getId()))
                .findFirst()
                .orElseThrow();
    }
}
