package nextstep.theme;

public final class ThemeJdbcSql {

    private ThemeJdbcSql() {
    }

    public static final String INSERT_INTO_STATEMENT = "INSERT INTO theme (name, desc, price) VALUES (?, ?, ?);";
    public static final String SELECT_BY_ID_STATEMENT = "SELECT id, name, desc, price from theme where id = ?;";
    public static final String SELECT_ALL_STATEMENT = "SELECT id, name, desc, price from theme;";
    public static final String DELETE_BY_ID = "DELETE FROM reservation where id = ?;";
}
