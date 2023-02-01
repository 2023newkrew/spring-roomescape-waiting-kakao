package com.nextstep.domains.theme;

import lombok.RequiredArgsConstructor;
import com.nextstep.domains.theme.dao.ThemeResultSetParser;
import com.nextstep.domains.theme.dao.ThemeStatementCreator;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class ThemeRepository {

    private final JdbcTemplate jdbcTemplate;

    private final ThemeStatementCreator statementCreator;

    private final ThemeResultSetParser resultSetParser;


    public Theme insert(Theme theme) throws DuplicateKeyException {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                connection -> statementCreator.createInsert(connection, theme),
                keyHolder
        );
        theme.setId(keyHolder.getKeyAs(Long.class));

        return theme;
    }

    public Theme getById(Long id) {
        return jdbcTemplate.query(
                connection -> statementCreator.createSelectById(connection, id),
                resultSetParser::parseSingleTheme
        );
    }

    public List<Theme> getAll() {
        return jdbcTemplate.query(
                statementCreator::createSelectAll,
                resultSetParser::parseAllThemes
        );
    }

    public boolean update(Long id, Theme theme) throws DuplicateKeyException {
        int updateRow = jdbcTemplate.update(connection -> statementCreator.createUpdate(connection, id, theme));

        return updateRow > 0;
    }
    
    public boolean delete(Long id) {
        int deleteRow = jdbcTemplate.update(connection -> statementCreator.createDeleteById(connection, id));

        return deleteRow > 0;
    }
}
