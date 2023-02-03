package nextstep.utils.batch.reader;

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

    public void setQuery(String queryString, RowMapper<T> rowMapper, Object... args) {
        setQueryString(queryString);
        setRowMapper(rowMapper);
        setArgs(args);
    }

    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }

    public void setRowMapper(RowMapper<T> rowMapper) {
        this.rowMapper = rowMapper;
    }

    public void setArgs(Object... args) {
        this.args = args;
    }

    @Override
    public List<T> read() {
        try {
            return jdbcTemplate.query(queryString, rowMapper, args);
        } catch (EmptyResultDataAccessException e) {
            return Collections.emptyList();
        }
    }
}
