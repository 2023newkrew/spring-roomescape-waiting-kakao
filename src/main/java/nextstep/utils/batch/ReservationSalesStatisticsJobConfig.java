package nextstep.utils.batch;

import lombok.RequiredArgsConstructor;
import nextstep.domain.saleshistory.SalesStatistics;
import nextstep.utils.JsonUtil;
import nextstep.utils.TransactionUtil;
import nextstep.utils.batch.model.SalesHistoryProjection;
import nextstep.utils.batch.processor.Processor;
import nextstep.utils.batch.processor.SalesStatisticsProcessor;
import nextstep.utils.batch.reader.JdbcTemplateNoOffsetNoOffsetPagingReader;
import nextstep.utils.batch.reader.NoOffsetPagingReader;
import nextstep.utils.batch.step.Step;
import nextstep.utils.batch.writer.Writer;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static nextstep.utils.batch.step.ChunkStep.*;

@RequiredArgsConstructor
@Configuration
public class ReservationSalesStatisticsJobConfig {

    private static final int CHUNK_SIZE = 20;

    private final JdbcTemplate jdbcTemplate;
    private final TransactionUtil transactionUtil;
    private final JsonUtil jsonUtil;
    private SalesStatistics salesStatistics;
    private LocalDateTime dateTime;

    public void set(LocalDateTime dateTime) {
        this.salesStatistics = new SalesStatistics(dateTime);
        this.dateTime = dateTime;
    }

    public Job transitionReservationStatusJob() {
        return Job.builder()
                .step(transitionReservationStatusStep())
                .build();
    }

    public Step transitionReservationStatusStep() {
        return ChunkStepBuilder.<SalesHistoryProjection, SalesStatistics>builder()
                .chunkSize(CHUNK_SIZE)
                .reader(reader())
                .processor(processor())
                .transactionUtil(transactionUtil)
                .build();
    }

    public NoOffsetPagingReader<SalesHistoryProjection> reader() {
        JdbcTemplateNoOffsetNoOffsetPagingReader<SalesHistoryProjection> reader = new JdbcTemplateNoOffsetNoOffsetPagingReader<>(jdbcTemplate);
        String readQueryString = "SELECT sales_history.id, sales_history.amount, theme.name " +
                "FROM sales_history " +
                "INNER JOIN theme ON theme.id = sales_history.theme_id " +
                "WHERE sales_history.created_at >= ?";
        RowMapper<SalesHistoryProjection> rowMapper = (rs, rowNum) -> new SalesHistoryProjection(
                rs.getLong("sales_history.id"),
                rs.getInt("sales_history.amount"),
                rs.getString("theme.name")
        );

        reader.setQuery(readQueryString, rowMapper, Map.of("tableIdColumnName", "sales_history.id", "created_at", dateTime));
        return reader;
    }

    public Processor<SalesHistoryProjection, SalesStatistics> processor() {
        return new SalesStatisticsProcessor(salesStatistics);
    }

    public void saveStatistics() {
        String salesPerTheme = jsonUtil.toString(salesStatistics.getSalesPerTheme());

        jdbcTemplate.update(
                "INSERT INTO reservation_sales_statistics (total_sales, sales_per_theme, date) VALUES (?, ?, ?)",
                salesStatistics.getTotalSales(), salesPerTheme, salesStatistics.getDateTime()
        );
    }

}
