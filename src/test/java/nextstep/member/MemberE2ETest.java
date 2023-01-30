package nextstep.member;

import nextstep.auth.AuthTestUtil;
import nextstep.auth.model.TokenRequest;
import nextstep.auth.model.TokenResponse;
import nextstep.member.model.Member;
import nextstep.member.model.MemberRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;

import static org.assertj.core.api.Assertions.assertThat;

@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
class MemberE2ETest {

    @DisplayName("존재하지 않는 memberName으로 멤버를 생성할 수 있다.")
    @Test
    void test1() {
        MemberRequest memberRequest = MemberTestUtil.NOT_EXIST_MEMBER.toDto();

        MemberTestUtil.createMemberAndGetValidatableResponse(memberRequest)
                .statusCode(HttpStatus.CREATED.value());
    }

    @DisplayName("이미 존재하는 memberName으로는 멤버를 생성할 수 없다.")
    @Test
    void test2() {
        MemberRequest memberRequest = MemberTestUtil.RESERVATION_EXIST_MEMBER_1.toDto();

        MemberTestUtil.createMemberAndGetValidatableResponse(memberRequest)
                .statusCode(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("토큰을 발급받은 유저는 자신의 정보를 조회할 수 있다.")
    @Test
    void test3() {
        TokenRequest tokenRequest = AuthTestUtil.RESERVATION_EXIST_MEMBER_TOKEN_REQUEST;
        final TokenResponse tokenResponse = AuthTestUtil.tokenLogin(tokenRequest);
        final String accessToken = tokenResponse.getAccessToken();

        Member member = MemberTestUtil.getMemberSelfInfo(accessToken);
        assertThat(member.getMemberName()).isEqualTo(tokenRequest.getMemberName());
    }

    @DisplayName("토큰이 유효하지 않은 유저는 내 정보를 조회할 수 없다.")
    @Test
    void test4() {
        MemberTestUtil.getMemberSelfInfoAndGetValidatableResponse("")
                        .statusCode(HttpStatus.UNAUTHORIZED.value());
    }
}
