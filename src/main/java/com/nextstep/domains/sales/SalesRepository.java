package com.nextstep.domains.sales;

import com.nextstep.domains.sales.dao.SalesResultSetParser;
import com.nextstep.domains.sales.dao.SalesStatementCreator;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class SalesRepository {

    private final JdbcTemplate jdbcTemplate;

    private final SalesStatementCreator statementCreator;

    private final SalesResultSetParser resultSetParser;
    public Sales insert(Sales sales) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                connection -> statementCreator.createInsert(connection, sales),
                keyHolder
        );
        sales.setId(keyHolder.getKeyAs(Long.class));

        return sales;
    }


    public Sales getByReservationId(Long reservationId) {
        return jdbcTemplate.query(
                connection -> statementCreator.createSelectByReservationId(connection, reservationId),
                resultSetParser::parseSales
        );
    }

    public List<Sales> getAll() {
        return jdbcTemplate.query(
                connection -> statementCreator.createSelectAll(connection),
                resultSetParser::parseSalesList
        );
    }

    public boolean updateById(Sales sales) {
        int updatedRow = jdbcTemplate.update(connection -> statementCreator.createUpdateById(connection, sales));

        return updatedRow > 0;
    }
}
