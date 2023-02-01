package nextstep;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.jdbc.support.MetaDataAccessException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Objects;

@Component
public class DatabaseCleaner {

    @Autowired
    private JdbcTemplate jdbcTemplate;
    private final List<String> tableNames = List.of("member", "schedule", "reservation", "theme", "waiting");

    @Transactional
    public void execute() {
        jdbcTemplate.update("SET REFERENTIAL_INTEGRITY FALSE");

        for (String tableName : tableNames) {
            jdbcTemplate.update("TRUNCATE TABLE " + tableName);
            jdbcTemplate.update("ALTER  TABLE " + tableName + " ALTER  COLUMN ID RESTART  WITH 1");
            jdbcTemplate.update("SET REFERENTIAL_INTEGRITY TRUE");
        }
    }

}
