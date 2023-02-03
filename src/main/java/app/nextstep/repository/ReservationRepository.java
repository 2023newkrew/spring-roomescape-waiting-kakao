package app.nextstep.repository;

import app.nextstep.dao.ReservationDao;
import app.nextstep.domain.Reservation;
import app.nextstep.entity.ReservationEntity;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class ReservationRepository {
    private ReservationDao reservationDao;

    public ReservationRepository(ReservationDao reservationDao) {
        this.reservationDao = reservationDao;
    }

    public Reservation findById(Long id) {
        return reservationDao.findById(id).toReservation();
    }

    public List<Reservation> findByThemeIdAndDate(Long themeId, LocalDate date) {
        List<Reservation> reservations = new ArrayList<>();
        for (ReservationEntity reservationEntity : reservationDao.findByThemeIdAndDate(themeId, Date.valueOf(date))) {
            reservations.add(reservationEntity.toReservation());
        }
        return reservations;
    }

    public List<Reservation> findByScheduleId(Long scheduleId) {
        List<Reservation> reservations = new ArrayList<>();
        for (ReservationEntity reservationEntity : reservationDao.findByScheduleId(scheduleId)) {
            reservations.add(reservationEntity.toReservation());
        }
        return reservations;
    }

    public Long save(Reservation reservation) {
        return reservationDao.save(
                reservation.getSchedule().getId(),
                reservation.getMember().getId(),
                reservation.getStatus());
    }

    public void delete(Long id) {
        reservationDao.deleteById(id);
    }
}
