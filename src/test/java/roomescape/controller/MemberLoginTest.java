package roomescape.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import roomescape.SpringWebApplication;
import roomescape.dto.LoginControllerTokenPostBody;
import roomescape.dto.LoginControllerTokenPostResponse;
import roomescape.dto.MemberControllerPostBody;
import roomescape.dto.MemberControllerPostResponse;
import roomescape.repository.MemberRepository;
import roomescape.service.JWTProvider;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("[로그인/유저]웹 요청 / 응답 처리로 입출력 추가")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = {SpringWebApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@ActiveProfiles({"web"})
public class MemberLoginTest {
    private final ObjectMapper mapper = new ObjectMapper();

    @Autowired
    private MockMvc mvc;

    @Autowired
    private MemberRepository repository;

    @Autowired
    private JWTProvider jwtProvider;

    @DisplayName("회원 가입")
    @Transactional
    @Test
    void createMember() throws Exception {
        var body = new MemberControllerPostBody(
                UUID.randomUUID().toString().split("-")[0],
                UUID.randomUUID().toString().split("-")[0],
                UUID.randomUUID().toString().split("-")[0],
                UUID.randomUUID().toString().split("-")[0]
        );
        var res = mvc.perform(post("/api/members")
                             .contentType(MediaType.APPLICATION_JSON_VALUE)
                             .content(mapper.writeValueAsString(body))
                     )
                     .andExpect(status().isCreated())
                     .andReturn();
        var memberId = mapper.readValue(res.getResponse().getContentAsString(), MemberControllerPostResponse.class)
                             .getId();
        var member = repository.selectById(memberId);
        assertAll(
                () -> assertThat(member.getName()).isEqualTo(body.getName()),
                () -> assertThat(member.getPhone()).isEqualTo(body.getPhone()),
                () -> assertThat(member.getUsername()).isEqualTo(body.getUsername()),
                () -> assertThat(member.getPassword()).isEqualTo(body.getPassword())
        );
    }

    @DisplayName("로그인 토큰 확인")
    @Transactional
    @Test
    void login() throws Exception {
        var res = mvc
                .perform(post("/api/login/token")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapper.writeValueAsString(new LoginControllerTokenPostBody("user0", "1q2w3e4r!")))
                )
                .andExpect(status().isCreated())
                .andReturn();
        var accessToken = mapper.readValue(res.getResponse()
                                              .getContentAsString(), LoginControllerTokenPostResponse.class)
                                .getAccessToken();
        assertThat(jwtProvider.getSubject(accessToken)).isEqualTo(Long.toString(2));
    }

    @DisplayName("자기 자신의 정보 확인")
    @Transactional
    @Test
    void me() throws Exception {
        var tokenRes = mvc
                .perform(post("/api/login/token")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(mapper.writeValueAsString(new LoginControllerTokenPostBody("user0", "1q2w3e4r!")))
                )
                .andExpect(status().isCreated())
                .andReturn();
        var token = mapper.readValue(tokenRes.getResponse()
                                             .getContentAsString(), LoginControllerTokenPostResponse.class)
                          .getAccessToken();
        mvc.perform(get("/api/members/me")
                   .header("Authorization", "Bearer " + token)
           )
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.id").value(2L))
           .andExpect(jsonPath("$.username").value("user0"))
           .andExpect(jsonPath("$.password").value("1q2w3e4r!"))
           .andExpect(jsonPath("$.name").value("일반유저0"))
           .andExpect(jsonPath("$.phone").value("010-0000-0000"))
           .andExpect(jsonPath("$.is_admin").value(false));
    }

    @DisplayName("토큰 없이 읽기 시도")
    @Test
    void invalidMeCall() throws Exception {
        mvc.perform(get("/api/members/me")
                   .header("Authorization", "Bearer ")
           )
           .andExpect(status().isUnauthorized());
    }
}
