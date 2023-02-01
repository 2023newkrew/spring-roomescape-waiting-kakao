package roomescape.controller;

import auth.controller.dto.LoginControllerTokenPostBody;
import auth.controller.dto.LoginControllerTokenPostResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeAll;
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
import roomescape.controller.dto.ReservationsControllerPostBody;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Random;
import java.util.UUID;
import java.util.regex.Pattern;

import static org.hamcrest.text.MatchesPattern.matchesPattern;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("예약 대기열 기능 확인")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = {SpringWebApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@ActiveProfiles({"web"})
public class WaitingTest {
    private final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());

    @Autowired
    private MockMvc mvc;

    private String ownerToken;
    private String otherToken;

    @BeforeAll
    void setup() throws Exception {
        ownerToken = mapper.readValue(
                                   mvc.perform(post("/api/login/token")
                                              .contentType(MediaType.APPLICATION_JSON_VALUE)
                                              .content(mapper.writeValueAsString(new LoginControllerTokenPostBody("user0", "1q2w3e4r!")))
                                      )
                                      .andExpect(status().isCreated())
                                      .andReturn()
                                      .getResponse()
                                      .getContentAsString(),
                                   LoginControllerTokenPostResponse.class)
                           .getAccessToken();
        otherToken = mapper.readValue(
                                   mvc.perform(post("/api/login/token")
                                              .contentType(MediaType.APPLICATION_JSON_VALUE)
                                              .content(mapper.writeValueAsString(new LoginControllerTokenPostBody("user1", "1q2w3e4r!")))
                                      )
                                      .andExpect(status().isCreated())
                                      .andReturn()
                                      .getResponse()
                                      .getContentAsString(),
                                   LoginControllerTokenPostResponse.class)
                           .getAccessToken();
    }


    @DisplayName("대기 가능한 예약 하기")
    @Transactional
    @Test
    void createWaiting() throws Exception {
        var rand = new Random();
        var date = LocalDate.of(rand.nextInt(2000, 2200), rand.nextInt(1, 12), rand.nextInt(1, 28));
        var time = LocalTime.of(rand.nextInt(0, 24), rand.nextInt(0, 60), 0);
        mvc.perform(post("/api/waiting")
                   .header("Authorization", "Bearer " + ownerToken)
                   .contentType(MediaType.APPLICATION_JSON_VALUE)
                   .content(mapper.writeValueAsString(new ReservationsControllerPostBody(
                           date,
                           time,
                           UUID.randomUUID().toString().split("-")[0],
                           1L
                   )))
           )
           .andExpect(status().isCreated())
           .andExpect(header().string("Location", matchesPattern(Pattern.compile("^/api/reservations/.+$"))))
        ;

        mvc.perform(post("/api/waiting")
                   .header("Authorization", "Bearer " + otherToken)
                   .contentType(MediaType.APPLICATION_JSON_VALUE)
                   .content(mapper.writeValueAsString(new ReservationsControllerPostBody(
                           date,
                           time,
                           UUID.randomUUID().toString().split("-")[0],
                           1L
                   )))
           )
           .andExpect(status().isCreated())
           .andExpect(header().string("Location", matchesPattern(Pattern.compile("^/api/waiting/.+$"))))
        ;
    }

    @DisplayName("내 대기목록 조회")
    @Test
    void showMyWaiting() throws Exception {
        mvc.perform(get("/api/waiting/mine")
                   .header("Authorization", "Bearer " + ownerToken)
           )
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.items.size()").value(2))
           .andExpect(jsonPath("$.items[0].name").value("대기대기1"))
           .andExpect(jsonPath("$.items[0].date").value("1970-01-01"))
           .andExpect(jsonPath("$.items[0].time").value("12:00"))
           .andExpect(jsonPath("$.items[0].theme_id").value(1L))
           .andExpect(jsonPath("$.items[0].theme_name").value("기본테마"))
           .andExpect(jsonPath("$.items[0].theme_desc").value("테마설명"))
           .andExpect(jsonPath("$.items[0].theme_price").value(1234000))
           .andExpect(jsonPath("$.items[0].wait_number").value(1))
           .andExpect(jsonPath("$.items[1].name").value("대기대기2"))
           .andExpect(jsonPath("$.items[1].date").value("1970-01-01"))
           .andExpect(jsonPath("$.items[1].time").value("12:00"))
           .andExpect(jsonPath("$.items[1].theme_id").value(1L))
           .andExpect(jsonPath("$.items[1].theme_name").value("기본테마"))
           .andExpect(jsonPath("$.items[1].theme_desc").value("테마설명"))
           .andExpect(jsonPath("$.items[1].theme_price").value(1234000))
           .andExpect(jsonPath("$.items[1].wait_number").value(2))
        ;
    }

    @DisplayName("예약 대기 취소")
    @Transactional
    @Test
    void deleteMyWaiting() throws Exception {
        mvc.perform(delete("/api/waiting/2")
                   .header("Authorization", "Bearer " + ownerToken)
           )
           .andExpect(status().isNoContent());
    }

    @DisplayName("다른 사람 예약 대기 취소")
    @Transactional
    @Test
    void deleteNotMyWaiting() throws Exception {
        mvc.perform(delete("/api/waiting/1")
                   .header("Authorization", "Bearer " + otherToken)
           )
           .andExpect(status().isForbidden());
    }

    @DisplayName("예약 취소시 자동으로 대기중인 요소를 예약시킨다.")
    @Transactional
    @Test
    void autoWaitingUpdate() throws Exception {
        mvc.perform(delete("/api/reservations/1")
                   .header("Authorization", "Bearer " + ownerToken)
           )
           .andExpect(status().isNoContent());
        mvc.perform(get("/api/waiting/mine")
                   .header("Authorization", "Bearer " + ownerToken)
           )
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.items.size()").value(1))
           .andExpect(jsonPath("$.items[0].name").value("대기대기2"))
           .andExpect(jsonPath("$.items[0].wait_number").value(1))
        ;
        mvc.perform(get("/api/reservations/mine")
                   .header("Authorization", "Bearer " + ownerToken)
           )
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.items.size()").value(1))
        ;
    }
}
