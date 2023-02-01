package roomescape.controller;

import auth.controller.dto.LoginControllerTokenPostBody;
import auth.controller.dto.LoginControllerTokenPostResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@DisplayName("웹 요청 / 응답 처리로 입출력 추가")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = {SpringWebApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.MOCK)
@AutoConfigureMockMvc
@ActiveProfiles({"web"})
public class ReservationTest {
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

    @DisplayName("예약 하기")
    @Transactional
    @Test
    void createReservation() throws Exception {
        var rand = new Random();
        mvc.perform(post("/api/reservations")
                   .header("Authorization", "Bearer " + ownerToken)
                   .contentType(MediaType.APPLICATION_JSON_VALUE)
                   .content(mapper.writeValueAsString(new ReservationsControllerPostBody(
                           LocalDate.of(rand.nextInt(2000, 2200), rand.nextInt(1, 12), rand.nextInt(1, 28)),
                           LocalTime.of(rand.nextInt(0, 24), rand.nextInt(0, 60), 0),
                           UUID.randomUUID().toString().split("-")[0],
                           1L
                   )))
           )
           .andExpect(status().isCreated());
    }

    @DisplayName("예약 조회")
    @Test
    void showReservation() throws Exception {
        mvc.perform(get("/api/reservations/1"))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.name").value("예약예약0"))
           .andExpect(jsonPath("$.date").value("1970-01-01"))
           .andExpect(jsonPath("$.time").value("12:00"))
           .andExpect(jsonPath("$.theme_id").value(1L))
           .andExpect(jsonPath("$.theme_name").value("기본테마"))
           .andExpect(jsonPath("$.theme_desc").value("테마설명"))
           .andExpect(jsonPath("$.theme_price").value(1234000))
        ;
    }

    @DisplayName("내 예약 조회")
    @Test
    void showMyReservation() throws Exception {
        mvc.perform(get("/api/reservations/mine")
                   .header("Authorization", "Bearer " + ownerToken)
           )
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.items.size()").value(1))
        ;
    }

    @DisplayName("내 예약이 아닌 경우에 대한 조회")
    @Test
    void showNotMyReservation() throws Exception {
        mvc.perform(get("/api/reservations/mine")
                   .header("Authorization", "Bearer " + otherToken)
           )
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.items.size()").value(0))
        ;
    }

    @DisplayName("예약 취소")
    @Transactional
    @Test
    void deleteReservation() throws Exception {
        mvc.perform(delete("/api/reservations/1")
                   .header("Authorization", "Bearer " + ownerToken)
           )
           .andExpect(status().isNoContent());
    }

    //
    @DisplayName("소유자가 아니라 삭제 불가능한 경우")
    @Transactional
    @Test
    void deleteReservationNotOwner() throws Exception {
        mvc.perform(delete("/api/reservations/1")
                   .header("Authorization", "Bearer " + otherToken)
           )
           .andExpect(status().isForbidden());
    }

    //
    @DisplayName("content-type이 application/json이 아닌 경우 값을 받지 않는다.")
    @Transactional
    @ParameterizedTest
    @ValueSource(strings = {
            MediaType.TEXT_PLAIN_VALUE,
            MediaType.TEXT_HTML_VALUE,
            MediaType.TEXT_XML_VALUE,
            MediaType.APPLICATION_XML_VALUE,
    })
    void notJson(String contentType) throws Exception {
        mvc.perform(post("/api/reservations")
                   .header("Authorization", "Bearer " + ownerToken)
                   .contentType(contentType)
                   .content("")
           )
           .andExpect(status().isUnsupportedMediaType());
    }

    //
    @DisplayName("예약 생성) 예약 생성 시 날짜와 시간이 똑같은 예약이 이미 있는 경우 예약을 생성할 수 없다.")
    @Transactional
    @Test
    void failToCreateReservationAlreadyExist() throws Exception {
        mvc.perform(post("/api/reservations")
                   .header("Authorization", "Bearer " + ownerToken)
                   .contentType(MediaType.APPLICATION_JSON_VALUE)
                   .content(mapper.writeValueAsString(new ReservationsControllerPostBody(
                           LocalDate.of(1970, 1, 1),
                           LocalTime.of(12, 0, 0),
                           UUID.randomUUID().toString().split("-")[0],
                           1L
                   )))
           )
           .andExpect(status().isConflict());
    }

    //
    @DisplayName("예약 조회) ID가 없는 경우 조회 불가")
    @Test
    void notExistID() throws Exception {
        mvc.perform(get("/api/reservations/0"))
           .andExpect(status().isNotFound());
    }

    //
    @DisplayName("예약 삭제) ID가 없는 경우 삭제 불가")
    @Transactional
    @Test
    void deleteNotExistId() throws Exception {
        mvc.perform(delete("/api/reservations/0")
                   .header("Authorization", "Bearer " + ownerToken)
           )
           .andExpect(status().isNotFound());
    }
}
