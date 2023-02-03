package nextstep.schedule;

public final class ScheduleJdbcSql {

    private ScheduleJdbcSql(){}

    public static final String INSERT_INTO_STATEMENT = "INSERT INTO schedule (theme_id, date, time) VALUES (?, ?, ?);";
    public static final String SELECT_BY_ID_STATEMENT =
            "SELECT schedule.id, schedule.theme_id, schedule.date, schedule.time, theme.id, theme.name, theme.desc, theme.price "
                    +
                    "from schedule " +
                    "inner join theme on schedule.theme_id = theme.id " +
                    "where schedule.id = ?;";
    public static final String SELECT_BY_THEME_ID_AND_DATE_STATEMENT =
            "SELECT schedule.id, schedule.theme_id, schedule.date, schedule.time, theme.id, theme.name, theme.desc, theme.price "
                    +
                    "from schedule " +
                    "inner join theme on schedule.theme_id = theme.id " +
                    "where schedule.theme_id = ? and schedule.date = ?;";
    public static final String DELETE_BY_ID = "DELETE FROM schedule where id = ?;";

}
