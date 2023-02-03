package nextstep.utils.batch;

import lombok.RequiredArgsConstructor;
import nextstep.domain.reservation.Reservation;
import nextstep.domain.reservation.ReservationProjection;
import nextstep.domain.saleshistory.SalesHistory;
import nextstep.domain.saleshistory.SalesHistoryStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static nextstep.domain.saleshistory.SalesHistoryStatus.PAYMENT;
import static nextstep.domain.saleshistory.SalesHistoryStatus.REFUND;

@RequiredArgsConstructor
@Configuration
public class ReservationTransitionStatusJobConfig {

    private final JdbcTemplate jdbcTemplate;
    private String statusName;
    private LocalDateTime startTime;

    public void setStatusNameAndStartTime(String statusName, LocalDateTime startTime) {
        this.statusName = statusName;
        this.startTime = startTime;
    }

    @Bean
    public Job transitionReservationStatusJob() {
        return Job.builder()
                .step(transitionReservationStatusStep())
                .build();
    }

    @Bean
    public Step transitionReservationStatusStep() {
        return Step.builder()
                .reader(reader())
                .processor(processor())
                .writer(writer())
                .build();
    }

    @Bean
    public Reader<ReservationProjection> reader() {
        JdbcTemplateReader<ReservationProjection> reader = new JdbcTemplateReader<>(jdbcTemplate);
        String readQueryString = "SELECT reservation.id, reservation.status, reservation.deposit, theme.id " +
                "FROM reservation" +
                "INNER JOIN schedule ON reservation.schedule_id = schedule.id" +
                "INNER JOIN theme ON schedule.theme_id = theme.id" +
                "WHERE reservation.status = ? AND ? < created_at";
        RowMapper<ReservationProjection> rowMapper = (rs, rowNum) -> new ReservationProjection(
                rs.getLong("id"),
                rs.getLong("theme_id"),
                rs.getString("status"),
                rs.getInt("deposit")
        );

        reader.setQuery(readQueryString, rowMapper, statusName, startTime);
        return reader;
    }

    @Bean
    public Processor<ReservationProjection, ReservationProjection> processor() {
        return ReservationProjection::transitStatus;
    }

    @Bean
    public Writer<ReservationProjection> writer() {
        CompositeWriter<ReservationProjection> compositeWriter = new CompositeWriter<>();
        compositeWriter.setWriters(reservationWriter(), salesHistoryWriter());

        return compositeWriter;
    }

    @Bean
    public JdbcTemplateBatchWriter<ReservationProjection, Reservation> reservationWriter() {
        JdbcTemplateBatchWriter<ReservationProjection, Reservation> reservationWriter = new JdbcTemplateBatchWriter<>(jdbcTemplate);
        reservationWriter.setQuery("UPDATE reservation SET status = ? WHERE id = ?", items -> {
            List<Object[]> batchArgs = new ArrayList<>();

            for (ReservationProjection item : items) {
                batchArgs.add(new Object[]{ item.getStatus().name(), item.getId() });
            }

            return batchArgs;
        });

        return reservationWriter;
    }

    @Bean
    public JdbcTemplateBatchWriter<ReservationProjection, SalesHistory> salesHistoryWriter() {
        JdbcTemplateBatchWriter<ReservationProjection, SalesHistory> salesHistoryWriter = new JdbcTemplateBatchWriter<>(jdbcTemplate);
        salesHistoryWriter.setQuery("INSERT INTO sales_history (theme_id, reservation_id, amount, status, created_at) VALUES (?, ?, ?, ?, ?", items -> {
            List<Object[]> batchArgs = new ArrayList<>();
            SalesHistoryStatus salesHistoryStatus = items.get(0).isApproved() ? PAYMENT : REFUND;

            for (ReservationProjection item : items) {
                batchArgs.add(new Object[]{ item.getThemeId(), item.getId(), item.getDeposit(), salesHistoryStatus, LocalDateTime.now() });
            }

            return batchArgs;
        });

        return salesHistoryWriter;
    }

}
