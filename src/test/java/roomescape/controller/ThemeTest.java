package roomescape.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
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
import roomescape.dto.ThemeControllerPostBody;

import java.util.Random;
import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("테마에 관련된 RESTful API 요청")
@SpringBootTest(classes = {SpringWebApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles({"web"})
public class ThemeTest {
    private final ObjectMapper mapper = new ObjectMapper()
            .registerModule(new JavaTimeModule());
    @Autowired
    private MockMvc mvc;

    private String adminToken;

    @BeforeEach
    void setUp() throws Exception {
        adminToken = mapper.readValue(
                mvc.perform(post("/api/login/token")
                           .contentType(MediaType.APPLICATION_JSON_VALUE)
                           .content(mapper.writeValueAsString(new LoginControllerTokenPostBody("admin", "1q2w3e4r!"))))
                   .andReturn()
                   .getResponse()
                   .getContentAsString(),
                LoginControllerTokenPostResponse.class
        ).getAccessToken();
    }

    @DisplayName("테마 생성")
    @Transactional
    @Test
    public void createTheme() throws Exception {
        mvc.perform(post("/api/themes")
                   .header("Authorization", "Bearer " + adminToken)
                   .contentType(MediaType.APPLICATION_JSON)
                   .content(mapper.writeValueAsString(new ThemeControllerPostBody(
                           UUID.randomUUID().toString().split("-")[0],
                           UUID.randomUUID().toString(),
                           new Random().nextInt(0, 1000000)
                   )))
           )
           .andExpect(status().isCreated());
    }

    @DisplayName("테마 검색")
    @Test
    public void getTheme() throws Exception {
        mvc.perform(get("/api/themes/1"))
           .andExpect(status().isOk())
           .andExpect(jsonPath("$.id").value(1L))
           .andExpect(jsonPath("$.name").value("기본테마"))
           .andExpect(jsonPath("$.desc").value("테마설명"))
           .andExpect(jsonPath("$.price").value(1234000))
        ;
    }

    @DisplayName("테마 삭제")
    @Transactional
    @Test
    public void deleteTheme() throws Exception {
        mvc.perform(delete("/api/themes/2")
                   .header("Authorization", "Bearer " + adminToken)
           )
           .andExpect(status().isNoContent());
        mvc.perform(get("/api/themes/2"))
           .andExpect(status().isNotFound());
    }

    @DisplayName("사용중인 테마 삭제")
    @Transactional
    @Test
    public void deleteUsingTheme() throws Exception {
        mvc.perform(delete("/api/themes/1")
                   .header("Authorization", "Bearer " + adminToken)
           )
           .andExpect(status().isConflict());
    }
}
