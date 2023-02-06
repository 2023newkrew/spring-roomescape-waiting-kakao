package nextstep.utils.batch.writer;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.function.Function;

@RequiredArgsConstructor
@Configuration
public class JdbcTemplateWriter<T> implements Writer<T> {

    private final JdbcTemplate jdbcTemplate;
    private String queryString;
    private Object[] args;
    private Function<List<T>, Object[]> argsFunction;

    public void setQuery(String queryString, Function<List<T>, Object[]> argsFunction) {
        this.queryString = queryString;
        this.argsFunction = argsFunction;
    }

    public void preWrite(List<T> items) {
        this.args = argsFunction.apply(items);
    }

    @Override
    public void write(List<T> items) {
        preWrite(items);
        jdbcTemplate.update(queryString, args);
    }
}
