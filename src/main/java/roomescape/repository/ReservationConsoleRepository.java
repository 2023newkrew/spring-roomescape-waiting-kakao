package roomescape.repository;

import org.springframework.stereotype.Component;
import roomescape.entity.Reservation;
import roomescape.entity.Theme;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class ReservationConsoleRepository {
    private static final String FIND_SQL = "select * from reservation where id = ?";
    private static final String COUNT_BY_DATETIME = "select count(*) from reservation where date = ? and time = ?";
    private static final String ADD_SQL = "insert into reservation (date, time, name, theme_name, theme_desc, theme_price) values (?, ?, ?, ?, ?, ?);";
    private static final String DELETE_SQL = "delete from reservation where id = ?";


    private final Connection connection;

    public ReservationConsoleRepository() throws SQLException {
        this.connection = DriverManager.getConnection("jdbc:h2:~/test;AUTO_SERVER=true");
    }

    private static List<Long> getKeys(PreparedStatement pstmt) throws SQLException {
        try (var genkeys = pstmt.getGeneratedKeys()) {
            var arrList = new ArrayList<Long>();
            while (genkeys.next()) {
                arrList.add(genkeys.getLong(1));
            }
            return arrList;
        }
    }

    private static Reservation intoReservation(ResultSet rset) throws SQLException {
        return new Reservation(
                rset.getLong("id"),
                rset.getDate("date").toLocalDate(),
                rset.getTime("time").toLocalTime(),
                rset.getString("name"),
                new Theme(
                        rset.getString("theme_name"),
                        rset.getString("theme_desc"),
                        rset.getInt("theme_price")
                )
        );
    }

    private List<Long> callPrepareStatementWithKeys(String sql, PrepareSetup fn) throws SQLException {
        try (var pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            fn.setup(pstmt);
            pstmt.executeUpdate();
            return getKeys(pstmt);
        }
    }

    private <T> T callPrepareStatementQuery(String sql, PrepareSetup fn, RunResultSet<T> runResultSet) throws SQLException {
        try (var pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            fn.setup(pstmt);
            return runResultSet.run(pstmt.executeQuery());
        }
    }

    private int callPrepareStatement(String sql, PrepareSetup fn) throws SQLException {
        try (var pstmt = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            fn.setup(pstmt);
            return pstmt.executeUpdate();
        }
    }

    public Optional<Long> addReservation(Reservation reservation) {
        var foundCount = findCountReservationByDateTime(reservation.getDate(), reservation.getTime());
        if (foundCount >= 1) {
            return Optional.empty();
        }
        try {
            var result = callPrepareStatementWithKeys(ADD_SQL, (psmt) -> {
                psmt.setDate(1, Date.valueOf(reservation.getDate()));
                psmt.setTime(2, Time.valueOf(reservation.getTime()));
                psmt.setString(3, reservation.getName());
                psmt.setString(4, reservation.getTheme().getName());
                psmt.setString(5, reservation.getTheme().getDesc());
                psmt.setInt(6, reservation.getTheme().getPrice());
            });
            return Optional.of(result.get(0));
        } catch (SQLException sqlException) {
            throw new RuntimeException(sqlException);
        }
    }

    public Optional<Reservation> findReservation(long id) {
        try {
            return callPrepareStatementQuery(FIND_SQL, (psmt) -> psmt.setLong(1, id), (rset -> {
                if (!rset.next()) {
                    return Optional.empty();
                }
                return Optional.of(intoReservation(rset));
            }));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Long findCountReservationByDateTime(LocalDate date, LocalTime time) {
        try {
            return callPrepareStatementQuery(COUNT_BY_DATETIME, (psmt) -> {
                psmt.setDate(1, Date.valueOf(date));
                psmt.setTime(2, Time.valueOf(time));
            }, (rset -> {
                rset.next();
                return rset.getLong(1);
            }));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public int deleteReservation(long id) {
        try {
            return callPrepareStatement(DELETE_SQL, (psmt -> psmt.setLong(1, id)));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public interface PrepareSetup {
        void setup(PreparedStatement psmt) throws SQLException;
    }

    public interface RunResultSet<T> {

        T run(ResultSet rset) throws SQLException;
    }
}
