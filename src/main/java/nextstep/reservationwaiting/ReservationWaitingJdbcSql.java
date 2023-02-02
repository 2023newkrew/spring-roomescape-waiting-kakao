package nextstep.reservationwaiting;

public final class ReservationWaitingJdbcSql {

    private ReservationWaitingJdbcSql(){}

    public static final String INSERT_INTO_STATEMENT = "INSERT INTO reservation_waiting (schedule_id, member_id, wait_num, wait_status) VALUES (?, ?, ?, ?);";
    public static final String SELECT_BY_MEMBER_ID_STATEMENT = "SELECT " +
            "reservation_waiting.id, reservation_waiting.member_id, reservation_waiting.wait_num, reservation_waiting.wait_status, " +
            "schedule.id, schedule.theme_id, schedule.date, schedule.time, " +
            "theme.id, theme.name, theme.desc, theme.price, " +
            "from reservation_waiting " +
            "inner join schedule on reservation_waiting.schedule_id = schedule.id " +
            "inner join theme on schedule.theme_id = theme.id " +
            "where reservation_waiting.member_id = ?;";
    public static final String EXIST_BY_ID_STATEMENT = "SELECT " +
            "1 " +
            "from reservation_waiting " +
            "where member_id = ? and id = ? " +
            "LIMIT 1;";
    public static final String SELECT_MAX_WAIT_NUM_BY_SCHEDULE_ID_STATEMENT = "SELECT " +
            "max(reservation_waiting.wait_num) " +
            "from reservation_waiting " +
            "where reservation_waiting.schedule_id = ? " +
            "AND reservation_waiting.wait_status in (3);";
    public static final String DELETE_BY_ID_STATEMENT = "DELETE FROM reservation_waiting where id = ?;";

    public static final String SELECT_BY_ID_STATEMENT = "SELECT " +
            "reservation_waiting.id, reservation_waiting.member_id, reservation_waiting.wait_num, reservation_waiting.wait_status, " +
            "schedule.id, schedule.theme_id, schedule.date, schedule.time, " +
            "theme.id, theme.name, theme.desc, theme.price, " +
            "from reservation_waiting " +
            "inner join schedule on reservation_waiting.schedule_id = schedule.id " +
            "inner join theme on schedule.theme_id = theme.id " +
            "where reservation_waiting.id = ? " +
            "AND RESERVATION_WAITING.MEMBER_ID = ?";

    public static final String COUNT_BY_WAIT_NUM_STATEMENT = "SELECT " +
            "count(id) " +
            "from reservation_waiting " +
            "where reservation_waiting.schedule_id = ? " +
            "AND reservation_waiting.wait_status in (3) " +
            "AND reservation_waiting.wait_num < (?) ";

    public static final String STATUS_TO_DROPPED_STATEMENT = "UPDATE reservation_waiting "
            + "SET wait_status = 4 "
            + "WHERE member_id = ? AND "
            + "id = ?;";
}
