package roomescape.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class SalesRepository {
    private static final String INSERT = "insert into sales (reason, change, reservation_id) values (:reason, :change, :reservation_id)";
    //    private static final RowMapper<Sales> ROW_MAPPER = (rs, rowNum) -> new Sales(
//            rs.getTimestamp(1).toLocalDateTime(),
//            rs.getString(2),
//            rs.getBigDecimal(3),
//            rs.getLong(4)
//    );
    private final NamedParameterJdbcTemplate jdbc;

    public void insert(String reason, BigDecimal change, Optional<Long> reservationId) {

        var params = new MapSqlParameterSource();
        params.addValue("reason", reason);
        params.addValue("change", change);
        params.addValue("reservation_id", reservationId.orElse(null));
        jdbc.update(INSERT, params);

    }
}
