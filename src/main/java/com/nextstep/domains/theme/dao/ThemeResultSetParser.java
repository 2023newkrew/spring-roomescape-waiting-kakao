package com.nextstep.domains.theme.dao;

import com.nextstep.domains.theme.Theme;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Component
public class ThemeResultSetParser {


    public Theme parseSingleTheme(ResultSet resultSet) throws SQLException {
        if (!resultSet.next()) {
            return null;
        }

        return parseTheme(resultSet);
    }

    private Theme parseTheme(ResultSet resultSet) throws SQLException {
        return new Theme(
                resultSet.getLong("id"),
                resultSet.getString("name"),
                resultSet.getString("desc"),
                resultSet.getInt("price")
        );
    }

    public List<Theme> parseAllThemes(ResultSet resultSet) throws SQLException {
        List<Theme> themes = new ArrayList<>();
        while (resultSet.next()) {
            themes.add(parseTheme(resultSet));
        }

        return themes;
    }
}
