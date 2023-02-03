package nextstep.utils.batch;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.function.Function;

@RequiredArgsConstructor
public class JdbcTemplateBatchWriter<T, V> implements Writer<T> {

    private final JdbcTemplate jdbcTemplate;
    private String queryString;
    private List<Object[]> batchArgs;
    private Function<List<T>, List<?>> batchArgsFunction;

    public void setQuery(String queryString, Function<List<T>, List<?>> batchArgsFunction) {
        this.queryString = queryString;
        this.batchArgsFunction = batchArgsFunction;
    }

    public void preWrite(List<T> items) {
        this.batchArgs = (List<Object[]>) batchArgsFunction.apply(items);
    }

    @Override
    public void write(List<T> items) {
        preWrite(items);
        jdbcTemplate.batchUpdate(queryString, batchArgs);
    }
}
