package nextstep.utils.batch;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.Collections;
import java.util.List;

@RequiredArgsConstructor
public class JdbcTemplateReader<T> implements Reader<T> {

    private final JdbcTemplate jdbcTemplate;
    private String queryString;
    private RowMapper<T> rowMapper;
    private Object[] args;

    @Override
    public List<T> read() {
        try {
            return jdbcTemplate.query(queryString, rowMapper, args);
        } catch (EmptyResultDataAccessException e) {
            return Collections.emptyList();
        }
    }

    public void setQuery(String queryString, RowMapper<T> rowMapper, Object... args) {
        this.queryString = queryString;
        this.rowMapper = rowMapper;
        this.args = args;
    }
}
