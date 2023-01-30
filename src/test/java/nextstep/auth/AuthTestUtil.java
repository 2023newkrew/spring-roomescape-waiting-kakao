package nextstep.auth;

import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import auth.model.TokenRequest;
import auth.model.TokenResponse;
import nextstep.member.model.Member;
import nextstep.member.MemberTestUtil;
import org.springframework.http.MediaType;

public class AuthTestUtil {
    public static final TokenRequest RESERVATION_EXIST_MEMBER_TOKEN_REQUEST = new TokenRequest(MemberTestUtil.RESERVATION_EXIST_MEMBER_1.getMemberName(), MemberTestUtil.RESERVATION_EXIST_MEMBER_1.getPassword());
    public static final TokenRequest RESERVATION_NOT_EXIST_MEMBER_TOKEN_REQUEST = new TokenRequest(MemberTestUtil.RESERVATION_NOT_EXIST_MEMBER.getMemberName(), MemberTestUtil.RESERVATION_NOT_EXIST_MEMBER.getPassword());

    public static TokenResponse tokenLoginForReservationExistMember() {
        return tokenLogin(RESERVATION_EXIST_MEMBER_TOKEN_REQUEST);
    }

    public static TokenResponse tokenLoginForReservationNotExistMember() {
        return tokenLogin(RESERVATION_NOT_EXIST_MEMBER_TOKEN_REQUEST);
    }

    public static TokenResponse tokenLogin(Member member) {
        return tokenLogin(new TokenRequest(member.getMemberName(), member.getPassword()));
    }

    public static TokenResponse tokenLogin(TokenRequest tokenRequest) {
        return createTokenAndGetValidatableResponse(tokenRequest)
                .extract().as(TokenResponse.class);
    }

    public static TokenRequest getNotExistMemberTokenRequest(){
        return new TokenRequest(MemberTestUtil.NOT_EXIST_MEMBER.getMemberName(), MemberTestUtil.NOT_EXIST_MEMBER.getPassword());
    }

    public static ValidatableResponse createTokenAndGetValidatableResponse(TokenRequest tokenRequest) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(tokenRequest)
                .when().post("/login/token")
                .then().log().all();
    }
}
