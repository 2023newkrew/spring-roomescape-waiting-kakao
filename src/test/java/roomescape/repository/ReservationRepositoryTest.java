package roomescape.repository;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import roomescape.SpringWebApplication;
import roomescape.entity.Theme;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DisplayName("데이터베이스와 상호작용 테스트")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(classes = {SpringWebApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles({"web"})
public class ReservationRepositoryTest {
    @Autowired
    private ReservationRepository repository;

    @Autowired
    private ThemeRepository themeRepository;

    @Autowired
    private NamedParameterJdbcTemplate jdbc;

    private Theme targetTheme = null;

    @BeforeAll
    void setupTheme() {
        var rand = new Random();
        targetTheme = themeRepository.selectById(themeRepository.insert(
                UUID.randomUUID().toString().split("-")[0],
                UUID.randomUUID().toString(),
                rand.nextInt(0, 10000000)
        )).get();
    }


    @DisplayName("삽입 테스트")
    @Test
    @Transactional
    void insert() {
        var rand = new Random();
        var name = UUID.randomUUID().toString().split("-")[0];
        var date = LocalDate.of(rand.nextInt(2000, 2200), rand.nextInt(1, 12), rand.nextInt(1, 28));
        var time = LocalTime.of(rand.nextInt(0, 24), rand.nextInt(0, 60), 0);
        var themeId = targetTheme.getId();
        var id = repository.insert(name, date, time, themeId);
        assertThat(id).isPresent();
        assertThat(jdbc.queryForObject(
                "select count(*) from reservation where id = :id and name = :name and date = :date and time = :time and theme_id = :themeId",
                Map.of(
                        "id", id.get(),
                        "name", name,
                        "date", date,
                        "time", time,
                        "themeId", themeId
                ),
                Integer.class
        ))
                .isEqualTo(1);
    }

    @DisplayName("조회 테스트")
    @Test
    @Transactional
    void selectById() {
        var rand = new Random();
        var name = UUID.randomUUID().toString().split("-")[0];
        var date = LocalDate.of(rand.nextInt(2000, 2200), rand.nextInt(1, 12), rand.nextInt(1, 28));
        var time = LocalTime.of(rand.nextInt(0, 24), rand.nextInt(0, 60), 0);
        var themeId = targetTheme.getId();
        var id = repository.insert(name, date, time, themeId);
        assertThat(id).isPresent();
        assertThat(repository.selectById(id.get()))
                .isPresent()
                .get()
                .satisfies((reservation) -> {
                    assertThat(reservation.getId()).isEqualTo(id.get());
                    assertThat(reservation.getName()).isEqualTo(name);
                    assertThat(reservation.getDate()).isEqualTo(date);
                    assertThat(reservation.getTime()).isEqualTo(time);
                    assertThat(reservation.getTheme().getId()).isEqualTo(themeId);
                });
    }

    @DisplayName("삭제 테스트")
    @Test
    @Transactional
    void delete() {
        var rand = new Random();
        var name = UUID.randomUUID().toString().split("-")[0];
        var date = LocalDate.of(rand.nextInt(2000, 2200), rand.nextInt(1, 12), rand.nextInt(1, 28));
        var time = LocalTime.of(rand.nextInt(0, 24), rand.nextInt(0, 60), 0);
        var themeId = targetTheme.getId();
        var id = repository.insert(name, date, time, themeId);
        assertThat(id).isPresent();
        assertThat(repository.selectById(id.get())).isPresent();
        assertThat(repository.delete(id.get())).isEqualTo(1);
        assertThat(repository.selectById(id.get())).isEmpty();
    }
}
