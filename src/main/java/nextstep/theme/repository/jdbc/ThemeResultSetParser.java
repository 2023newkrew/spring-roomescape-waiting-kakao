package nextstep.theme.repository.jdbc;

import nextstep.theme.domain.ThemeEntity;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class ThemeResultSetParser {


    public ThemeEntity parseSingleTheme(ResultSet resultSet) throws SQLException {
        if (!resultSet.next()) {
            return null;
        }

        return parseTheme(resultSet);
    }

    private ThemeEntity parseTheme(ResultSet resultSet) throws SQLException {
        return new ThemeEntity(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getString("desc"),
                resultSet.getInt("price")
        );
    }

    public List<ThemeEntity> parseAllThemes(ResultSet resultSet) throws SQLException {
        List<ThemeEntity> themes = new ArrayList<>();
        while (resultSet.next()) {
            themes.add(parseTheme(resultSet));
        }

        return themes;
    }
}
