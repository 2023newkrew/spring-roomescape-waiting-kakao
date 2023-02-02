package roomwaiting.nextstep;

import io.restassured.RestAssured;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestExecutionListeners;
import roomwaiting.AcceptanceTestExecutionListener;
import roomwaiting.auth.token.TokenRequest;
import roomwaiting.auth.token.TokenResponse;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import roomwaiting.nextstep.config.SecurityConfig;
import roomwaiting.nextstep.member.Member;
import roomwaiting.nextstep.member.MemberDao;
import roomwaiting.nextstep.member.MemberRequest;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import roomwaiting.nextstep.member.MemberService;

@SpringBootTest(classes = {RoomEscapeApplication.class})
@TestExecutionListeners(value = {AcceptanceTestExecutionListener.class,}, mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS)
public class AbstractE2ETest {
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String ADMIN = "ADMIN";

    protected TokenResponse token;

    @BeforeEach
    protected void setUp() {
        MemberRequest memberBody = new MemberRequest(USERNAME, PASSWORD, "name", "010-1234-5678", "ADMIN");
        RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(memberBody)
                .when().post("/members")
                .then().log().all()
                .statusCode(HttpStatus.CREATED.value());

        TokenRequest tokenBody = new TokenRequest(USERNAME, PASSWORD);
        var response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(tokenBody)
                .when().post("/login/token")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();

        token = response.as(TokenResponse.class);
    }

    public static Member saveMember(JdbcTemplate jdbcTemplate, String username, String password, String role){
        ApplicationContext ac = new AnnotationConfigApplicationContext(SecurityConfig.class);
        PasswordEncoder passwordEncoder = ac.getBean(PasswordEncoder.class);
        MemberRequest member = new MemberRequest(username, passwordEncoder.encode(password), username, "010", role);
        MemberService memberService = new MemberService(new MemberDao(jdbcTemplate), passwordEncoder);
        Long id = memberService.create(member);
        return new Member(id, member.getUsername(), member.getPassword(), member.getName(), member.getPhone(), member.getRole());
    }

    public static ExtractableResponse<Response> generateToken(String username, String password) {
        TokenRequest body = new TokenRequest(username, password);
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(body)
                .when().post("/login/token")
                .then().log().all()
                .statusCode(HttpStatus.OK.value())
                .extract();
    }
}
