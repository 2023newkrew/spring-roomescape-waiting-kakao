package roomescape.repository;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import roomescape.entity.Theme;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class ThemeRepository {
    private static final String SELECT_PAGE = "select theme.id, theme.name, theme.desc, theme.price from theme order by id limit :limit offset :offset";
    private static final String SELECT_BY_ID = "select theme.id, theme.name, theme.desc, theme.price from theme where id = :id";
    private static final String INSERT = "insert into theme(name, desc, price) values (:name, :desc, :price)";
    private static final String DELETE_THEME = "delete from theme where id = :id";
    private static final RowMapper<Theme> ROW_MAPPER = (rs, rowNum) -> new Theme(
            rs.getLong(1),
            rs.getString(2),
            rs.getString(3),
            rs.getInt(4)
    );
    private final NamedParameterJdbcTemplate jdbc;

    public ThemeRepository(NamedParameterJdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }


    public Long insert(String name, String desc, int price) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbc.update(INSERT, new MapSqlParameterSource(Map.ofEntries(
                Map.entry("name", name),
                Map.entry("desc", desc),
                Map.entry("price", price)
        )), keyHolder);
        return keyHolder.getKeyAs(Long.class);
    }

    public Optional<Theme> selectById(long id) {
        try {
            return Optional.ofNullable(jdbc.queryForObject(SELECT_BY_ID, Map.of("id", id), ROW_MAPPER));
        } catch (DataAccessException e) {
            return Optional.empty();
        }
    }

    public List<Theme> selectPage(int size, int page) {
        return jdbc.queryForStream(
                           SELECT_PAGE,
                           Map.of("limit", size, "offset", size * page),
                           ROW_MAPPER
                   )
                   .collect(Collectors.toList());
    }

    public int delete(long id) {
        try {
            return jdbc.update(DELETE_THEME, Map.of("id", id));
        } catch (DataAccessException sqlException) {
            return 0;
        }
    }
}
