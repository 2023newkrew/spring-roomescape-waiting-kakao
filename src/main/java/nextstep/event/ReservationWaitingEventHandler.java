package nextstep.event;

import lombok.RequiredArgsConstructor;
import nextstep.reservation.ReservationDao;
import nextstep.reservationwaiting.ReservationWaiting;
import nextstep.reservationwaiting.ReservationWaitingDao;
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
        reservationWaitingDao.findMinIdByScheduleId(reservationDeleteEvent.getSchedule())
                .ifPresent(this::convertReservationWaitingToReservation);
    }

    private void convertReservationWaitingToReservation(ReservationWaiting reservationWaiting) {
        reservationWaitingDao.deleteById(reservationWaiting.getId());
        reservationDao.save(reservationWaiting.toReservation());
    }
}