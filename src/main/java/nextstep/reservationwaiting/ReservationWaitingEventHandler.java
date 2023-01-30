package nextstep.reservationwaiting;

import lombok.RequiredArgsConstructor;
import nextstep.reservation.ReservationDao;
import nextstep.reservation.ReservationDeleteEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ReservationWaitingEventHandler {
    private final ReservationDao reservationDao;
    private final ReservationWaitingDao reservationWaitingDao;

    @EventListener
    @Transactional
    public void convert(ReservationDeleteEvent reservationDeleteEvent) {
        ReservationWaiting reservationWaiting = reservationWaitingDao.findMinIdByScheduleId(reservationDeleteEvent.getSchedule());
        if (reservationWaiting == null) {
            return;
        }
        reservationWaitingDao.deleteById(reservationWaiting.getId());
        reservationDao.save(reservationWaiting.toReservation());
    }
}