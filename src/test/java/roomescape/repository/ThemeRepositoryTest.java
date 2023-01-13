package roomescape.repository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import roomescape.SpringWebApplication;

import java.util.Map;
import java.util.UUID;
import java.util.random.RandomGenerator;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DisplayName("데이터베이스와 상호작용 테스트")
@SpringBootTest(classes = {SpringWebApplication.class}, webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles({"web"})
public class ThemeRepositoryTest {
    @Autowired
    private ThemeRepository repository;

    @Autowired
    private NamedParameterJdbcTemplate jdbc;

    @DisplayName("삽입 테스트")
    @Test
    @Transactional
    void insert() {
        var rand = RandomGenerator.getDefault();
        var name = UUID.randomUUID().toString().split("-")[0];
        var desc = UUID.randomUUID().toString();
        var price = rand.nextInt(1000, 100000000);
        var id = repository.insert(name, desc, price);
        assertThat(jdbc.queryForObject(
                "select count(*) from theme where id = :id and name = :name and desc = :desc and price = :price",
                Map.of(
                        "id", id,
                        "name", name,
                        "desc", desc,
                        "price", price
                ),
                Integer.class
        ))
                .isEqualTo(1);
    }

    @DisplayName("조회 테스트")
    @Test
    @Transactional
    void selectById() {
        var rand = RandomGenerator.getDefault();
        var name = UUID.randomUUID().toString().split("-")[0];
        var desc = UUID.randomUUID().toString();
        var price = rand.nextInt(1000, 100000000);
        var id = repository.insert(name, desc, price);
        assertThat(repository.selectById(id))
                .isPresent()
                .get()
                .satisfies((reservation) -> {
                    assertThat(reservation.getId()).isEqualTo(id);
                    assertThat(reservation.getName()).isEqualTo(name);
                    assertThat(reservation.getDesc()).isEqualTo(desc);
                    assertThat(reservation.getPrice()).isEqualTo(price);
                });
    }

    @DisplayName("삭제 테스트")
    @Test
    @Transactional
    void delete() {
        var rand = RandomGenerator.getDefault();
        var name = UUID.randomUUID().toString().split("-")[0];
        var desc = UUID.randomUUID().toString();
        var price = rand.nextInt(1000, 100000000);
        var id = repository.insert(name, desc, price);
        assertThat(repository.selectById(id)).isPresent();
        assertThat(repository.delete(id)).isEqualTo(1);
        assertThat(repository.selectById(id)).isEmpty();
    }
}
