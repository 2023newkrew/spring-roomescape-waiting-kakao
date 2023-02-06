package nextstep.utils.batch.reader;

import nextstep.utils.batch.model.PagingModel;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.util.List;
import java.util.Map;
import java.util.Objects;

public class JdbcTemplateNoOffsetNoOffsetPagingReader<T extends PagingModel> extends JdbcTemplateReader<T> implements NoOffsetPagingReader<T> {

    private int chunkSize = 20;
    private Long lastItemId;
    private Map<String, Object> parameterValues;
    private String baseQuery;

    public JdbcTemplateNoOffsetNoOffsetPagingReader(JdbcTemplate jdbcTemplate) {
        super(jdbcTemplate);
    }

    public void setChunkSize(int chunkSize) {
        this.chunkSize = chunkSize;
    }

    @Override
    public void setLastItemId(Long lastItemId) {
        this.lastItemId = lastItemId;
    }

    public void setQuery(String queryString, RowMapper<T> rowMapper, Map<String, Object> parameterValues) {
        this.baseQuery = queryString;
        setRowMapper(rowMapper);
        this.parameterValues = parameterValues;
    }

    @Override
    public List<T> read() {
        if (Objects.isNull(lastItemId)) {
            setQueryString(baseQuery + "LIMIT ?");
            setArgs(parameterValues.get("created_at"), chunkSize);
        } else {
            setQueryString(baseQuery + String.format("AND %s > ? LIMIT ?", parameterValues.get("tableIdColumnName")));
            setArgs(parameterValues.get("created_at"), lastItemId, chunkSize);
        }

        List<T> items = super.read();
        setLastItemId(items.get(items.size() - 1).getId());
        return items;
    }

}
