package nextstep.reservation;

import java.util.List;

public interface ReservationDao {
    Long save(Reservation reservation);

    List<Reservation> findAllByThemeIdAndDate(Long themeId, String date);

    Reservation findById(Long id);

    List<Reservation> findByScheduleId(Long id);

    void deleteById(Long id);
}
