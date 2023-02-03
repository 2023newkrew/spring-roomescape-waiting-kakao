package roomwaiting.nextstep.dbmapper;

import roomwaiting.nextstep.member.Member;
import roomwaiting.nextstep.reservation.domain.Reservation;
import roomwaiting.nextstep.reservation.domain.ReservationWaiting;
import roomwaiting.nextstep.sales.Sales;
import roomwaiting.nextstep.schedule.Schedule;
import roomwaiting.nextstep.theme.Theme;
import org.springframework.jdbc.core.RowMapper;

public interface DatabaseMapper {
    RowMapper<Member> memberRowMapper();
    RowMapper<Reservation> reservationRowMapper();
    RowMapper<Schedule> scheduleRowMapper();
    RowMapper<Theme> themeRowMapper();
    RowMapper<ReservationWaiting> reservationWaitingRowMapper();
    RowMapper<Sales> salesRowMapper();
}
