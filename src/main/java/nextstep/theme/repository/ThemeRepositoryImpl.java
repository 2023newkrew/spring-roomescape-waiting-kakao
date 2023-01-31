package nextstep.theme.repository;

import lombok.RequiredArgsConstructor;
import nextstep.theme.domain.ThemeEntity;
import nextstep.theme.repository.jdbc.ThemeResultSetParser;
import nextstep.theme.repository.jdbc.ThemeStatementCreator;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Repository
public class ThemeRepositoryImpl implements ThemeRepository {

    private final JdbcTemplate jdbcTemplate;

    private final ThemeStatementCreator statementCreator;

    private final ThemeResultSetParser resultSetParser;


    @Override
    public ThemeEntity insert(ThemeEntity theme) throws DuplicateKeyException {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(
                connection -> statementCreator.createInsert(connection, theme),
                keyHolder
        );
        theme.setId(keyHolder.getKeyAs(Long.class));

        return theme;
    }

    @Override
    public ThemeEntity getById(Long id) {
        return jdbcTemplate.query(
                connection -> statementCreator.createSelectById(connection, id),
                resultSetParser::parseSingleTheme
        );
    }

    @Override
    public List<ThemeEntity> getAll() {
        return jdbcTemplate.query(
                statementCreator::createSelectAll,
                resultSetParser::parseAllThemes
        );
    }

    @Override
    public boolean update(Long id, ThemeEntity theme) throws DuplicateKeyException {
        int updateRow = jdbcTemplate.update(connection -> statementCreator.createUpdate(connection, id, theme));

        return updateRow > 0;
    }

    @Override
    public boolean delete(Long id) {
        int deleteRow = jdbcTemplate.update(connection -> statementCreator.createDeleteById(connection, id));

        return deleteRow > 0;
    }
}
