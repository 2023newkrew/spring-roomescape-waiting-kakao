package roomescape.repository;


import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import roomescape.SpringWebApplication;

import java.util.Map;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("[사용자] 데이터베이스와 상호작용 테스트")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = {SpringWebApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles({"web"})
public class MemberRepositoryTest {
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private NamedParameterJdbcTemplate jdbc;

    @DisplayName("삽입 테스트")
    @Test
    @Transactional
    void insert() {
        var name = UUID.randomUUID().toString().split("-")[0];
        var phone = "010-1111-1111";
        var username = UUID.randomUUID().toString().split("-")[0];
        var password = UUID.randomUUID().toString().split("-")[0];
        var id = memberRepository.insert(username, password, name, phone);
        var count = jdbc.queryForObject(
                "select count(*) from member where id = :id and name = :name and username = :username and password = :password and phone = :phone",
                Map.of(
                        "id", id,
                        "name", name,
                        "phone", phone,
                        "username", username,
                        "password", password
                ), Integer.class);
        assertThat(count).isEqualTo(1);
    }

    @DisplayName("선택 테스트")
    @Test
    @Transactional
    void select() {
        var name = UUID.randomUUID().toString().split("-")[0];
        var phone = "010-1111-1111";
        var username = UUID.randomUUID().toString().split("-")[0];
        var password = UUID.randomUUID().toString().split("-")[0];
        var id = memberRepository.insert(username, password, name, phone);
        assertThat(jdbc.queryForObject(
                "select count(*) from member where id = :id and name = :name and phone = :phone and username = :username and password = :password",
                Map.of(
                        "id", id,
                        "name", name,
                        "phone", phone,
                        "username", username,
                        "password", password
                ),
                Integer.class
        ))
                .isEqualTo(1);
    }
}
